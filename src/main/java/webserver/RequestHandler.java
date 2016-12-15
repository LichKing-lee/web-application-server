package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FormService;
import service.UserService;
import util.HttpRequestUtils;
import util.IOUtils;
import web.RequestParser;
import web.GetRequestParser;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String DEFAULT_INDEX = "/index.html";

    private Socket connection;
    private FormService formService;
    private UserService userService;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.formService = new FormService();
        this.userService = new UserService();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            if(line == null){
                return;
            }

            String uri = getUri(line);
            log.debug("resource :: {}", uri);

            if(!formService.isStaticUri(uri)){
                Map<String, String> map;
                if(isGet(line)) {
                    map = HttpRequestUtils.parseQueryString(line.split("\\?")[1]);
                }else{
                    String sss;
                    int length = 0;
                    while(!"".equals(sss = reader.readLine())){
                        if(sss == null){
                            return;
                        }

                        if(sss.contains("Content-Length")){
                            length = Integer.parseInt(sss.split(" ")[1]);
                        }
                    }
                    String str = IOUtils.readData(reader, length);
                    log.debug("postData :: {}", str);
                    map = HttpRequestUtils.parseQueryString(str);
                }
                User user = new User(map);
                log.debug("user :: {}", user);
                DataBase.addUser(user);

                try {
                    DataOutputStream dos = new DataOutputStream(out);
                    dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
                    dos.writeBytes("Location: http://localhost:9090/index.html\r\n");
                    dos.writeBytes("\r\n");
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
                return;
            }

            byte[] body = Files.readAllBytes(new File("./web-application-server/webapp" + uri).toPath());

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isGet(String line) {
        return line.contains("GET");
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getUri(String line){
        return line.split(" ")[1];
    }
}

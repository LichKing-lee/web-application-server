package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FormService;
import service.UserService;
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
            List<String> lines = IOUtils.makeList(new BufferedReader(new InputStreamReader(in)));
            log.debug("lines :: {}", lines);

            if(lines.isEmpty()){
                return;
            }

            String uri = getUri(lines);
            log.debug("resource :: {}", uri);

            if(!formService.isStaticUri(uri)){
                userService.saveUser(lines);
                uri = DEFAULT_INDEX;
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

    private String getUri(List<String> lines){
        return lines.get(0).split(" ")[1];
    }

    private RequestParser parserFactory(){
        return new GetRequestParser();
    }
}

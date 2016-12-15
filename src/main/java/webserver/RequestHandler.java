package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String STATIC_RESOURCE = "([^\\s]+(\\.(?i)(html|js|css))$)";
    private static final String DEFAULT_INDEX = "/index.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            List<String> lines = new ArrayList<>();
            while(!"".equals(line = br.readLine())){
                if(line == null){
                    return;
                }
                lines.add(line);
            }

            log.debug("lines :: {}", lines);
            String requestResource = lines.get(0).split(" ")[1];
            log.debug("resource :: {}", requestResource);

            if(!Pattern.matches(STATIC_RESOURCE, requestResource)){
                Map<String, String> map = HttpRequestUtils.parseQueryString(requestResource.split("\\?")[1]);

                User user = new User(map);
                log.debug("user :: {}", user);
                requestResource = DEFAULT_INDEX;
            }

            byte[] body = Files.readAllBytes(new File("./web-application-server/webapp" + requestResource).toPath());

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
}

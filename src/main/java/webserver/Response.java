package webserver;

import enumclass.ResponseCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by LichKing on 2016. 12. 18..
 */
public class Response {
    private String header;
    private String resource;
    private byte[] body;

    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String LOCAL_LOCATION = "Location: http://localhost:9090";
    private static final String DEFAULT_PATH = "./web-application-server/webapp";

    public static Response create300AndCookie(ResponseCode responseCode, String resource){
        Response response = new Response();
        response.resource = DEFAULT_PATH + resource;
        response.body = new byte[0];

        StringBuilder builder = new StringBuilder();

        builder.append(HTTP_VERSION).append(" ").append(responseCode.getMessage()).append("\r\n");
        builder.append(LOCAL_LOCATION).append(resource).append("\r\n");
        builder.append("Set-Cookie: logined=true").append("\r\n");
        builder.append("\r\n");

        response.header = builder.toString();

        return response;
    }

    public static Response create300(ResponseCode responseCode, String resource){
        Response response = new Response();
        response.resource = DEFAULT_PATH + resource;
        response.body = new byte[0];

        StringBuilder builder = new StringBuilder();

        builder.append(HTTP_VERSION).append(" ").append(responseCode.getMessage()).append("\r\n");
        builder.append(LOCAL_LOCATION).append(resource).append("\r\n");
        builder.append("\r\n");

        response.header = builder.toString();

        return response;
    }

    public static Response create200(ResponseCode responseCode, String resource){
        Response response = new Response();
        response.resource = DEFAULT_PATH + resource;

        try {
            response.body = Files.readAllBytes(new File(response.resource).toPath());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        StringBuilder builder = new StringBuilder();

        builder.append(HTTP_VERSION).append(" ").append(responseCode.getMessage()).append("\r\n");
        builder.append("Content-Type: text/html;charset=utf-8").append("\r\n");
        builder.append("Content-Length: ").append(response.body.length).append("\r\n");
        builder.append("\r\n");

        response.header = builder.toString();

        return response;
    }

    public String getHeader(){
        return this.header;
    }

    public byte[] getBody(){
        return this.body;
    }
}

package controller;

import enumclass.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;
import webserver.Response;

import java.util.regex.Pattern;

/**
 * Created by LichKing on 2016. 12. 18..
 */
public class RequestController {
    private Logger log = LoggerFactory.getLogger(RequestController.class);
    private UserController userController;
    private static final String INDEX_PAGE = "/index.html";
    private static final String STATIC_RESOURCE = "([^\\s]+(\\.(?i)(html|js|css))$)";

    public RequestController(){
        this.userController = new UserController();
    }

    public Response handle(Request request){
        String requestUri = request.getRequestUri();
        Response response = null;

        if(isStaticUri(requestUri)){
            return Response.create200(ResponseCode.OK, requestUri);
        }

        if("/user/create".equals(requestUri)){
            this.userController.saveUser(request);
            requestUri = INDEX_PAGE;
            response = Response.create300(ResponseCode.FOUND, requestUri);
        }

        return response;
    }

    private boolean isStaticUri(String uri){
        return Pattern.matches(STATIC_RESOURCE, uri);
    }
}

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
    private static final String LOGIN_FAIL_PAGE = "/user/login_failed.html";
    private static final String STATIC_RESOURCE = "([^\\s]+(\\.(?i)(html|js|css))$)";

    public RequestController(){
        this.userController = new UserController();
    }

    public Response handle(Request request){
        String requestUri = request.getRequestUri();

        if(isStaticUri(requestUri)){
            return Response.create200(ResponseCode.OK, requestUri);
        }

        return forward(request);
    }

    private boolean isStaticUri(String uri){
        return Pattern.matches(STATIC_RESOURCE, uri);
    }

    private Response forward(Request request){
        switch (request.getRequestUri()){
            case "/user/create" :
                this.userController.saveUser(request);
                return Response.create300(ResponseCode.FOUND, INDEX_PAGE);

            case "/user/login" :
                boolean isLogin = this.userController.login(request);
                return Response.create300AndCookie(ResponseCode.FOUND, isLogin ? INDEX_PAGE : LOGIN_FAIL_PAGE);

            default:
                return null;
        }
    }
}

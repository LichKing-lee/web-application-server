package controller;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enumclass.ResponseCode;
import webserver.Request;
import webserver.Response;

/**
 * Created by LichKing on 2016. 12. 18..
 */
public class RequestController {
	private Logger log = LoggerFactory.getLogger(RequestController.class);
	private UserController userController;
	private static final String STATIC_RESOURCE = "([^\\s]+(\\.(?i)(html|js|css|ico|woff|ttf))$)";

	public RequestController() {
		this.userController = new UserController();
	}

	public Response handle(Request request) {
		String requestUri = request.getRequestUri();

		if (isStaticUri(requestUri)) {
		    return new Response(ResponseCode.OK, requestUri);
		}

		return forward(request);
	}

	private boolean isStaticUri(String uri) {
		return Pattern.matches(STATIC_RESOURCE, uri);
	}

	private Response forward(Request request) {
		Response response;
		switch (request.getRequestUri()) {
			case "/user/create":
				response = new Response(ResponseCode.FOUND);
				this.userController.saveUser(request, response);
				return response;

			case "/user/login":
				response = new Response(ResponseCode.OK);
				this.userController.login(request, response);
				return response;

			case "/user/list":
				response = new Response(ResponseCode.OK);
				this.userController.list(request, response);
				return response;
			default:
				throw new IllegalArgumentException("null resource :: " + request.getRequestUri());
		}
	}
}

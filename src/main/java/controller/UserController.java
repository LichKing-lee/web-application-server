package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import webserver.Request;
import webserver.Response;

/**
 * Created by LichKing on 2016. 12. 15..
 */
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private static final String LOGIN_FAIL_PAGE = "/user/login_failed.html";
	private static final String INDEX_PAGE = "/index.html";

	public void saveUser(Request request, Response response) {
		User user = new User(request.getParameterMap());
		log.debug("user :: {}", user);
		DataBase.addUser(user);
		response.setRedirectPage(INDEX_PAGE);
	}

	public void login(Request request, Response response) {
		User user = DataBase.findUserById(request.getParameter("userId"));
		log.debug("user :: {}", user);

		if (user != null && user.isMatchPassword(request.getParameter("password"))) {
			response.addCookie("logined", true);
			response.setRedirectPage(INDEX_PAGE);
			return;
		}

		response.setRedirectPage(LOGIN_FAIL_PAGE);
	}

	public void list(Request request, Response response) {
		if (!"true".equals(request.getCookie("logined"))) {
			response.setRedirectPage("/user/login.html");
		}
	}
}

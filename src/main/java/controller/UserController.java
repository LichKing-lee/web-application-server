package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;

/**
 * Created by LichKing on 2016. 12. 15..
 */
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public void saveUser(Request request){
        User user = new User(request.getParameterMap());
        log.debug("user :: {}", user);
        DataBase.addUser(user);
    }

    public boolean login(Request request){
        User user = DataBase.findUserById(request.getParameter("userId"));
        log.debug("user :: {}", user);

        return user != null && user.isMatchPassword(request.getParameter("password"));
    }
}

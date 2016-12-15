package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import web.GetRequestParser;
import web.RequestParser;
import webserver.RequestHandler;

import java.util.List;
import java.util.Map;

/**
 * Created by LichKing on 2016. 12. 15..
 */
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void saveUser(List<String> lines){
        RequestParser requestParser = parserFactory(lines);
        Map<String, String> map = HttpRequestUtils.parseQueryString(requestParser.getUri(lines).split("\\?")[1]);
        User user = new User(map);
        log.debug("user :: {}", user);
        DataBase.addUser(user);
    }

    private RequestParser parserFactory(List<String> lines){
        return new GetRequestParser();
    }
}

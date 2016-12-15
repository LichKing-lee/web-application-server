package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import web.GetRequestParser;
import web.PostRequestParser;
import web.RequestParser;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

/**
 * Created by LichKing on 2016. 12. 15..
 */
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void saveUser(BufferedReader reader){
//        RequestParser requestParser = parserFactory(lines);
//        Map<String, String> map = HttpRequestUtils.parseQueryString(requestParser.getData(lines).split("\\?")[1]);
//        User user = new User(map);
//        log.debug("user :: {}", user);
//        DataBase.addUser(user);
    }

    private RequestParser parserFactory(List<String> lines){
        if(lines.get(0).split(" ")[0].equals("GET")){
            return new GetRequestParser();
        }
        return new PostRequestParser();
    }
}

package web;

import java.util.List;

/**
 * Created by LichKing on 2016. 12. 15..
 */
public class GetRequestParser implements RequestParser {
    @Override
    public String getUri(List<String> lines){
        return lines.get(0).split(" ")[1];
    }
}

package web;

import java.util.List;

/**
 * Created by LichKing on 2016. 12. 15..
 */
public interface RequestParser {
    String getUri(List<String> lines);
}

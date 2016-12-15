package service;

import java.util.regex.Pattern;

/**
 * Created by LichKing on 2016. 12. 15..
 */
public class FormService {
    private static final String STATIC_RESOURCE = "([^\\s]+(\\.(?i)(html|js|css))$)";

    public boolean isStaticUri(String uri){
        return Pattern.matches(STATIC_RESOURCE, uri);
    }
}

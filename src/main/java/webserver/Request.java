package webserver;

import enumclass.RequestMethod;
import util.HttpRequestUtils;

import java.util.Map;

/**
 * Created by LichKing on 2016. 12. 16..
 */
public class Request {
    private RequestMethod requestMethod;
    private int contentLength;
    private String host;
    private String userAgent;
    private String requestUri;
    private Map<String, String> parameterMap;

    public String getRequestUri(){
        return this.requestUri;
    }

    public void setRequestUri(String requestUri){
        this.requestUri = requestUri;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getHost() {
        return host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setParameterMap(Map<String, String> parameterMap){
        this.parameterMap = parameterMap;
    }

    public String getParameter(String key){
        return this.parameterMap.get(key);
    }

    public void setRequestMethodByString(String method){
        this.requestMethod = RequestMethod.valueOf(method);
    }

    public void setGetInfo(String[] requestUri){
        this.requestUri = requestUri[0];

        if(requestUri.length > 1){
            this.parameterMap = HttpRequestUtils.parseQueryString(requestUri[1]);
        }
    }

    public Map<String, String> getParameterMap() {
        return this.parameterMap;
    }
}

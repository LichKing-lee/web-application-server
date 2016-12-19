package webserver;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by LichKing on 2016. 12. 17..
 */
public class RequestTest {
    private String requestMessage;

    @Before
    public void setUp(){
        this.requestMessage = "GET /user/form.html HTTP/1.1\n" +
                "Host: localhost:9090\n" +
                "Connection: keep-alive\n" +
                "Pragma: no-cache\n" +
                "Cache-Control: no-cache\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Accept-Encoding: gzip, deflate, sdch, br\n" +
                "Accept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4\n" +
                "Cookie: Webstorm-c9a63f48=90e5814a-d1c8-4b24-983c-4f61675288d1; Idea-decf4e01=61e22adc-cabc-4c98-995a-e90ccf095cf6; JSESSIONID=E9CCE5EF7D003A4CCEE64D4D15074F36";
    }

    @Test
    public void request_테스트(){
        String method = this.requestMessage.split("\n")[0].split(" ")[0];

        assertThat(method, is("GET"));
    }
}

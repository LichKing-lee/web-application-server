import org.junit.Before;
import org.junit.Test;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

/**
 * Created by LichKing on 2016. 12. 15..
 */
public class ParseTest {
    private String resource;

    @Before
    public void setUp(){
        this.resource = "/user/create?userId=lcy1111&password=1234&name=a&email=a%40a";
    }

    @Test
    public void 파싱_테스트1(){
        //given
        String queryString = this.resource.split("\\?")[1];

        //when

        //then
        assertThat(queryString, is("userId=lcy1111&password=1234&name=a&email=a%40a"));
    }

    @Test
    public void 파싱_테스트2(){
        //given
        String queryString = this.resource.split("\\?")[1];

        //when
        String[] arr = queryString.split("&");

        Map<String, String> map = new HashMap<>();
        for(String str : arr){
            String[] temp = str.split("=");
            map.put(temp[0], temp[1]);
        }

        //then
        assertThat(map.get("userId"), is("lcy1111"));
        assertThat(map.get("password"), is("1234"));
        assertThat(map.get("name"), is("a"));
    }

    @Test
    public void 유틸_테스트(){
        //given
        String queryString = this.resource.split("\\?")[1];

        //when
        Map<String, String> map = HttpRequestUtils.parseQueryString(queryString);

        //then
        assertThat(map.get("userId"), is("lcy1111"));
        assertThat(map.get("password"), is("1234"));
        assertThat(map.get("name"), is("a"));
    }

    @Test
    public void 정규식_테스트(){
        //given
        String pattern = "([^\\s]+(\\.(?i)(html|png|gif|bmp))$)";

        //when


        //then
        assertTrue(Pattern.matches(pattern, "abc.html"));
    }
}

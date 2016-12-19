package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by LichKing on 2016. 12. 19..
 */
public class ResponseHeader {
	private Map<String, Object> headerMap;

	{
		headerMap = new HashMap<>();
		headerMap.put("Content-Type", "text/html;charset=utf-8");
	}

	public String get(String key) {
		return String.valueOf(this.headerMap.get(key));
	}

	public <T> T get(String key, Class<T> clazz) {
		return this.headerMap.get(key) == null ? null : clazz.cast(this.headerMap.get(key));
	}

	public void put(String key, Object value) {
		this.headerMap.put(key, value);
	}

	public Set<Map.Entry<String, Object>> entries() {
		return this.headerMap.entrySet();
	}
}

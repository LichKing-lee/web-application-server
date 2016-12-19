package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import enumclass.ResponseCode;

/**
 * Created by LichKing on 2016. 12. 18..
 */
public class Response {
	private ResponseHeader responseHeader;
	private ResponseCode responseCode;
	private String resource;
	private byte[] body;
	private String header;
	private String redirectPage;

	private static final String HTTP_VERSION = "HTTP/1.1";
	private static final String LOCAL_LOCATION = "Location: http://localhost:9090";
	//private static final String LOCAL_LOCATION = "Location: http://localhost:8080";
	private static final String DEFAULT_PATH = "./web-application-server/webapp";
	private static final String INDEX_PAGE = "/index.html";

	public Response() {
		this(ResponseCode.FOUND);
	}

	public Response(ResponseCode responseCode) {
		this(responseCode, null);
	}

	public Response(ResponseCode responseCode, String resource) {
		this.responseCode = responseCode;
		this.responseHeader = new ResponseHeader();
		this.resource = resource;
		this.body = new byte[0];
	}

	public static Response create200(ResponseCode responseCode) {
		return create200(responseCode, INDEX_PAGE);
	}

	public static Response create200(ResponseCode responseCode, String resource) {
		Response response = new Response(ResponseCode.OK);
		response.resource = resource;

		try {
			response.body = Files.readAllBytes(new File(response.resource).toPath());
			//response.body = Files.readAllBytes(new File("D:/workspace/web-application-server/webapp" + resource).toPath());
		} catch (IOException e) {
			throw new IllegalArgumentException(resource);
		}

		StringBuilder builder = new StringBuilder();

		builder.append(HTTP_VERSION).append(" ").append(responseCode.getMessage()).append("\r\n");
		builder.append("Content-Type: text/html;charset=utf-8").append("\r\n");
		builder.append("Content-Length: ").append(response.body.length).append("\r\n");

		if (response.responseHeader.get("Set-Cookie", Map.class) != null) {
			builder.append("Set-Cookie: logined=").append(response.responseHeader.get("Set-Cookie", Map.class).get("logined"));
		}

		builder.append("\r\n");

		response.header = builder.toString();

		return response;
	}

	public String getHeader() {
		return this.responseCode.getHeader(this);
	}

	public byte[] getBody() {
		return this.body;
	}

	public void addCookie(String key, Object value) {
		@SuppressWarnings("unchecked")
		Map<String, String> cookieMap = this.responseHeader.get("Set-Cookie", Map.class);

		if (cookieMap == null) {
			cookieMap = new HashMap<>();
		}

		cookieMap.put(key, String.valueOf(value));
		this.responseHeader.put("Set-Cookie", cookieMap);
	}

	public void setRedirectPage(String redirectPage) {
		this.resource = redirectPage;
	}

	public ResponseHeader getResponseHeader() {
		return this.responseHeader;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public String getResource() {
		return this.resource;
	}
}

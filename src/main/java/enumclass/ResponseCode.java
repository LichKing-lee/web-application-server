package enumclass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import webserver.HeaderMakeable;
import webserver.Response;

/**
 * Created by LichKing on 2016. 12. 18..
 */
public enum ResponseCode implements HeaderMakeable {
	OK(200) {
		@Override
		public String getHeader(Response response) {
			try {
				//response.body = Files.readAllBytes(new File(response.resource).toPath());
				response.setBody(Files.readAllBytes(new File("D:/workspace/web-application-server/webapp" + response.getResource()).toPath()));
			} catch (IOException e) {
				throw new IllegalArgumentException();
			}

			StringBuilder builder = new StringBuilder();

			builder.append(HTTP_VERSION).append(" ").append(this.getMessage()).append("\r\n");
			if (response.getResource().endsWith(".css")) {
				builder.append("Content-Type: text/css;charset=utf-8").append("\r\n");
			} else {
				builder.append("Content-Type: text/html;charset=utf-8").append("\r\n");
			}
			builder.append("Content-Length: ").append(response.getBody().length).append("\r\n");

			if (response.getResponseHeader().get("Set-Cookie", Map.class) != null) {
				builder.append("Set-Cookie: logined=").append(response.getResponseHeader().get("Set-Cookie", Map.class).get("logined")).append
						("\r\n");
			}

			builder.append("\r\n");

			return builder.toString();
		}
	}, FOUND(302) {
		@Override
		public String getHeader(Response response) {
			StringBuilder builder = new StringBuilder();

			builder.append(HTTP_VERSION).append(" ").append(this.getMessage()).append("\r\n");
			builder.append(LOCAL_LOCATION).append(response.getResource()).append("\r\n");

			builder.append("\r\n");

			return builder.toString();
		}
	};

	private static final String HTTP_VERSION = "HTTP/1.1";
	//private static final String LOCAL_LOCATION = "Location: http://localhost:9090";
	private static final String LOCAL_LOCATION = "Location: http://localhost:8080";
	private final Integer code;

	ResponseCode(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.code + " " + this.name();
	}
}

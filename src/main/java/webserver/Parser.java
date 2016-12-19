package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import enumclass.RequestMethod;
import util.HttpRequestUtils;
import util.IOUtils;

/**
 * Created by LichKing on 2016. 12. 16..
 */
public class Parser {
	private static final String METHOD_PATTER = "GET|POST|PUT|DELETE";

	public Request createRequest(BufferedReader reader) throws IOException {
		Request request = new Request();
		String firstLine = reader.readLine();

		if (firstLine == null) {
			return null;
		}

		request.setRequestMethodByString(firstLine.split(" ")[0]);

		String line;
		List<String> lines = new ArrayList<>();
		int length = 0;
		while (!"".equals(line = reader.readLine())) {
			lines.add(line);

			if (line.contains("Content-Length")) {
				length = Integer.parseInt(line.split(" ")[1]);
			}

			if (line.startsWith("Cookie")) {
				request.setCookieMap(HttpRequestUtils.parseCookies(line.split(": ")[1]));
			}
		}

		if (RequestMethod.GET == request.getRequestMethod()) {
			String[] requestUri = firstLine.split(" ")[1].split("\\?");
			request.setGetInfo(requestUri);
		}

		if (RequestMethod.POST == request.getRequestMethod()) {
			request.setRequestUri(firstLine.split(" ")[1]);
			request.setParameterMap(HttpRequestUtils.parseQueryString(IOUtils.readData(reader, length)));
		}

		return request;
	}
}

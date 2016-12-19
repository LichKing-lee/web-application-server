package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.RequestController;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;
	private RequestController requestController;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
		this.requestController = new RequestController();
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				  connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			Parser parser = new Parser();
			Request request = parser.createRequest(reader);

			if (request == null) {
				return;
			}

			Response response = requestController.handle(request);
			response(response, out);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response(Response response, OutputStream out) throws IOException {
		// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeBytes(response.getHeader());
		dos.write(response.getBody(), 0, response.getBody().length);
		log.debug("body :: {}", response.getBody().length);
		dos.flush();
	}
}

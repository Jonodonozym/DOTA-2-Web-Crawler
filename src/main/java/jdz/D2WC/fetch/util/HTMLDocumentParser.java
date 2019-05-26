
package jdz.D2WC.fetch.util;

import static jdz.D2WC.fetch.util.RateLimiter.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Random;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLDocumentParser {
	private static final int DEFAULT_WAIT = 2000;
	private static final int BLOCKED_WAIT = 305000;
	
	private final Logger logger = LoggerFactory.getLogger("DOTA2");
	private final Random random = new Random();

	public Document getDocument(String page) throws IOException {
		waitIfTooFast((int) (DEFAULT_WAIT * (1 + random.nextDouble())));
		logger.info("Connecting to: " + page);
		try {
			Connection.Response response = loadJSComponents(Jsoup.connect(page).ignoreHttpErrors(true)).timeout(2000)
					.execute();

			if (response.statusCode() == 200)
				return response.parse();

			else if (response.statusCode() == 429) {
				logger.info("HTTP code 429 Too Many Requests, waiting " + BLOCKED_WAIT + " ms");
				waitIfTooFast(BLOCKED_WAIT);
				return getDocument(page);
			}

			else
				throw new HttpStatusException(response.statusMessage(), response.statusCode(), page);
		}
		catch (SocketTimeoutException e) {
			logger.info("timed out, retrying");
			return getDocument(page);
		}
	}

	public Connection loadJSComponents(Connection connection) {
		return connection.userAgent(
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
				.followRedirects(true).maxBodySize(0);
	}
}


package jdz.D2WC.fetch.abstractClasses;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLDocumentParser {
	private static long lastConnect = System.currentTimeMillis();
	private static Random random = new Random();
	private static final int DEFAULT_WAIT = 500;
	private static final int BLOCKED_WAIT = 1500;

	private final Logger logger = LoggerFactory.getLogger("DOTA2");

	private void waitIfTooFast(int waitTimeMS) {
		long timeDiff = System.currentTimeMillis() - lastConnect;
		if (timeDiff < waitTimeMS)
			try {
				Thread.sleep(waitTimeMS - timeDiff + random.nextInt(1000));
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		lastConnect = System.currentTimeMillis();
	}

	public Document getDocument(String page) throws IOException {
		waitIfTooFast(DEFAULT_WAIT);
		try {
			logger.info("Connecting to: " + page);
			return loadJSComponents(Jsoup.connect(page)).get();
		}
		catch (HttpStatusException | SocketTimeoutException e) {
			waitIfTooFast(BLOCKED_WAIT);
			return getDocument(page);
		}
	}

	public Connection loadJSComponents(Connection connection) {
		return connection.header("Accept-Encoding", "gzip, deflate")
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
				.referrer("http://www.google.com").followRedirects(true).maxBodySize(0);
	}
}

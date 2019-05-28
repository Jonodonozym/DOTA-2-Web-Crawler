
package jdz.D2WC.fetch.util;

import static jdz.D2WC.fetch.util.RateLimiter.waitIfTooFast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JSONParser<T> {
	private final Logger logger = LoggerFactory.getLogger("DOTA2");
	public static final int API_WAIT = 1200;

	protected abstract T parse(String source);

	protected T readJSONFromUrl(String url) throws IOException, JSONException {
		waitIfTooFast(API_WAIT);
		logger.info("Connecting to: " + url);
		InputStream is = getInputStream(url);
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			return parse(jsonText);
		}
		finally {
			is.close();
		}
	}

	public static InputStream getInputStream(String url) throws IOException {
		HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setDoOutput(true);
		connection.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
		connection.setInstanceFollowRedirects(true);
		return connection.getInputStream();
	}

	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}

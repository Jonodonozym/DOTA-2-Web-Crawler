
package jdz.D2WC.fetch.util;

import static jdz.D2WC.fetch.util.RateLimiter.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;

public abstract class JSONParser<T> {
	public static final int API_WAIT = 1200;
	
	protected abstract T parse(String source);

	protected T readJSONFromUrl(String url) throws IOException, JSONException {
		waitIfTooFast(API_WAIT);
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			return parse(jsonText);
		}
		finally {
			is.close();
		}
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

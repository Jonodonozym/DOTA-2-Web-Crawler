
package jdz.D2WC.dataIO.fetch;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLDocumentParser {
	public Document getDocument(String page) throws IOException {
		return loadJSComponents(Jsoup.connect(page)).execute().parse();
	}

	public Connection loadJSComponents(Connection connection) {
		return connection
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
				.referrer("http://www.google.com").followRedirects(true).maxBodySize(0);
	}
}

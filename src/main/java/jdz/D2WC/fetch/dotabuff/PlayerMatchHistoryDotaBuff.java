
package jdz.D2WC.fetch.dotabuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jdz.D2WC.fetch.util.HTMLDocumentParser;
import jdz.D2WC.fetch.util.HTMLTableParser;

public class PlayerMatchHistoryDotaBuff extends HTMLDocumentParser {
	public static interface MatchIDRunnable {
		public void run(long playerID);
	}

	public void forEachMatchID(long playerID, int maxMatches, MatchIDRunnable runnable) throws IOException {
		String profileURL = "https://www.dotabuff.com/players/" + playerID;
		int pages = getNumPages(profileURL);
		for (int page = 1; page < pages; page++)
			for (long matchID : parser.getAll(profileURL, page)) {
				if (maxMatches == 0)
					return;
				runnable.run(matchID);
				maxMatches--;
			}
	}

	private final TableParser parser = new TableParser();

	public List<Long> getMatchIDs(long playerID) throws IOException {
		List<Long> matchIDs = new ArrayList<>();
		forEachMatchID(playerID, -1, (e) -> matchIDs.add(e));
		return matchIDs;
	}

	private int getNumPages(String profileURL) throws IOException {
		Document document = getDocument(profileURL + "/matches");
		Element lastPageElement = document.select("nav[class='pagination']").first().children().last();
		if (!lastPageElement.className().equals("last"))
			return 1;

		String url = lastPageElement.child(0).attr("href");
		return Integer.parseInt(url.substring(url.lastIndexOf('=') + 1));
	}

	private class TableParser extends HTMLTableParser<Long> {
		@Override
		protected String getPage(Object... state) {
			return state[0] + "/matches?enhance=overview&page=" + state[1];
		}

		@Override
		protected Elements getTableRows(Document document) {
			Element table = document.select("div[class='content-inner']").first().child(0).child(1).child(0).child(0);
			return table.selectFirst("tbody").children();
		}

		@Override
		protected Long extractFromRow(Elements columns) {
			String url = columns.get(1).selectFirst("a").attr("href");
			return Long.parseLong(url.substring(url.lastIndexOf('/') + 1));
		}
	}
}

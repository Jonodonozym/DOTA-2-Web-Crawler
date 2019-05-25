
package jdz.D2WC.fetch.dotabuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import jdz.D2WC.fetch.abstractClasses.HTMLDocumentParser;
import jdz.D2WC.fetch.abstractClasses.HTMLTableParser;

public class TopPlayersList extends HTMLDocumentParser {
	public static interface PlayerIDRunnable {
		public void run(long playerID);
	}

	private final LeaderboardTableParser tableParser = new LeaderboardTableParser();

	public void forEachPlayerID(PlayerIDRunnable runnable) throws IOException {
		int pages = getNumPages(getDocument("https://www.dotabuff.com/players/leaderboard"));
		for (int page = 0; page < pages; page++)
			for (long playerID: tableParser.getAll(page))
				runnable.run(playerID);
	}

	public List<Long> getTopPlayerIDs() throws IOException {
		List<Long> players = new ArrayList<>();
		forEachPlayerID((player) -> players.add(player));
		return players;
	}

	private int getNumPages(Document document) {
		String href = document.selectFirst("span[class='last']").child(0).attr("href");
		return Integer.parseInt(href.substring(href.lastIndexOf("=") + 1));
	}

	private class LeaderboardTableParser extends HTMLTableParser<Long> {
		@Override
		protected String getPage(Object... state) {
			return "https://www.dotabuff.com/players/leaderboard?page=" + state[0];
		}

		@Override
		protected Elements getTableRows(Document document) {
			return document.selectFirst("table[class='leaderboard-ranks-table']").child(1).children();
		}

		@Override
		protected Long extractFromRow(Elements columns) {
			String href = columns.get(1).child(0).attr("href");
			return Long.parseLong(href.substring(href.lastIndexOf("/") + 1));
		}

	}
}

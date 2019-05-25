
package jdz.D2WC.fetch.dotabuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jdz.D2WC.entity.enums.Lane;
import jdz.D2WC.entity.hero.Hero;
import jdz.D2WC.entity.match.Match;
import jdz.D2WC.entity.match.PlayerMatchStats;
import jdz.D2WC.fetch.abstractClasses.HTMLDocumentParser;

public class MatchFetcher extends HTMLDocumentParser {

	public Match getMatchData(long matchID) throws IOException {
		Document document = fetchDocument(matchID);

		List<PlayerMatchStats> playerStats = getPlayerStats(document);
		int radiantScore = getRadiantScore(document);
		int direScore = getDireScore(document);
		int timeSeconds = getMatchTime(document);

		return new Match(matchID, playerStats, radiantScore, direScore, timeSeconds);
	}

	private Document fetchDocument(long matchID) throws IOException {
		return getDocument("https://www.dotabuff.com/matches/" + matchID);
	}

	private List<PlayerMatchStats> getPlayerStats(Document document) {
		List<PlayerMatchStats> stats = new ArrayList<>();
		for (int table : new int[] { 0, 1 }) {
			Elements rows = selectTableRows(document, table);
			for (Element row : rows)
				stats.add(parseTableRow(row.children(), table == 0));
		}
		return stats;
	}

	private Elements selectTableRows(Document document, int tableIndex) {
		Element table = document.select("article[class='r-tabbed-table']").get(tableIndex).child(0);
		return table.children().select("tbody").first().children();
	}

	private PlayerMatchStats parseTableRow(Elements columns, boolean radiant) {
		String heroURLRef = columns.get(0).child(0).child(0).attr("href");
		Hero hero = new Hero(heroURLRef.substring(heroURLRef.lastIndexOf('/') + 1), null);

		int columnCounter = 5;
		Lane lane = null;

		try {
			lane = Lane.fromDotaBuffLabel(columns.get(2).child(0).attr("title"));
		}
		catch (Exception e) {
			columnCounter -= 2;
		}

		long player = getPlayerIDFromRow(columns);

		int kills = extractNumber(columns.get(columnCounter++));
		int deaths = extractNumber(columns.get(columnCounter++));
		int assists = extractNumber(columns.get(columnCounter++));

		int goldWorth = extractNumber(columns.get(columnCounter));
		String goldEarnedTooltip = columns.get(columnCounter).child(0).attr("title");
		int goldEarned = parseScientificNumber(goldEarnedTooltip.substring(0, goldEarnedTooltip.indexOf(' ')).trim());

		return new PlayerMatchStats(-1, player, hero, lane, radiant, kills, deaths, assists, goldWorth, goldEarned);
	}

	private long getPlayerIDFromRow(Elements columns) {
		Element nameColumn = columns.select("td[class='tf-pl single-lines']").first();
		if (nameColumn.text().contains("Anonymous"))
			return -1;
		String profileURLRef = nameColumn.children().select("a").last().attr("href");
		String[] words = profileURLRef.substring(profileURLRef.lastIndexOf('/') + 1).split("-");
		return Integer.parseInt(words[0]);
	}

	private int extractNumber(Element element) {
		if (element.hasText())
			return parseScientificNumber(element.text());
		for (Element child : element.children()) {
			int childResult = extractNumber(child);
			if (childResult != -1)
				return childResult;
		}
		return -1;
	}

	private int parseScientificNumber(String number) {
		int radix = 1;
		if (number.endsWith("k"))
			radix = 1000;
		return (int) (Double.parseDouble(number.replaceAll("[^\\d.-]", "")) * radix);
	}

	private int getMatchTime(Document document) {
		String[] parts = document.selectFirst("span[class='duration']").text().split(":");
		int seconds = 0;
		int secondsPerPart = 1;
		for (int i = parts.length - 1; i >= 0; i--) {
			seconds += Integer.parseInt(parts[i]) * secondsPerPart;
			secondsPerPart *= 60;
		}
		return seconds;
	}

	private int getRadiantScore(Document document) {
		return Integer.parseInt(document.selectFirst("span[class='the-radiant score']").text());
	}

	private int getDireScore(Document document) {
		return Integer.parseInt(document.selectFirst("span[class='the-dire score']").text());
	}

}

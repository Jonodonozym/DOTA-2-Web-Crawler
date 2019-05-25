
package jdz.D2WC.fetch.dotabuff;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jdz.D2WC.entity.enums.Lane;
import jdz.D2WC.entity.player.PlayerSummary;
import jdz.D2WC.entity.player.PlayerSummary.PlayerSummaryBuilder;
import jdz.D2WC.fetch.abstractClasses.HTMLDocumentParser;

public class PlayerFetcher extends HTMLDocumentParser {
	public PlayerSummary fetchSummary(long playerID) throws IOException {
		PlayerSummaryBuilder builder = PlayerSummary.builder();

		builder.playerID(playerID);

		Document document = getDocument("https://www.dotabuff.com/players/" + playerID + "/scenarios?metric=all");
		Elements headerContent = getHeaderContent(document);

		builder.MMR(getHeaderContentValue("Solo MMR", headerContent));
		setWinsLosses(builder, document);
		setLanesSummary(builder, document);

		return builder.build();
	}

	private Elements getHeaderContent(Document document) {
		return document.selectFirst("div[class='header-content-secondary']").children().select("dl");
	}

	private int getHeaderContentValue(String key, Elements headerContent) {
		Element node = getHeaderContentNode(key, headerContent);
		if (node == null)
			return -1;
		
		String text = node.text().replaceAll(",", "");
		if (text.contains(" "))
			text = text.substring(0, text.indexOf(" "));
		
		return Integer.parseInt(text);
	}

	private Element getHeaderContentNode(String key, Elements headerContent) {
		for (Element element : headerContent)
			if (element.children().select("dt").text().equals(key))
				return element.children().select("dd").first();
		return null;
	}

	private void setWinsLosses(PlayerSummaryBuilder builder, Document document) {
		Element row = getTableRow(document, "All Matches");
		setAllWinsLosses(builder, row);
		setRecentWinsLosses(builder, row);
	}

	private Element getTableRow(Document document, String key) {
		for (Element element : getTableRows(document))
			for (Element child : element.children())
				if (child.text().startsWith(key))
					return child;
		return null;
	}

	private Elements getTableRows(Document document) {
		return document.selectFirst("article[class='r-tabbed-table']").child(0).children().select("tbody");
	}

	private void setAllWinsLosses(PlayerSummaryBuilder builder, Element row) {
		int matches = Integer.parseInt(row.child(1).text().replace(",", "").replaceAll("-", "0"));
		double winRatio = Double.parseDouble(row.child(2).text().replaceAll("%", "")) / 100.0;

		int wins = (int) Math.round(matches * winRatio);
		builder.gamesWon(wins);
		builder.gamesLost(matches - wins);
	}

	private void setRecentWinsLosses(PlayerSummaryBuilder builder, Element row) {
		int matches = Integer.parseInt(row.child(4).text().replace(",", "").replaceAll("-", "0"));
		double winRatio = Double.parseDouble(row.child(5).text().replaceAll("%", "")) / 100.0;

		int wins = (int) Math.round(matches * winRatio);
		builder.gamesWon6Months(wins);
		builder.gamesLost6Months(matches - wins);
	}

	private void setLanesSummary(PlayerSummaryBuilder builder, Document document) {
		Map<Lane, Integer> all = new HashMap<>();
		Map<Lane, Integer> recent = new HashMap<>();
		for (Lane lane : Lane.values()) {
			Element row = getTableRow(document, lane.getDotabuffLabel());
			all.put(lane,
					row == null ? 0 : Integer.parseInt(row.child(1).text().replace(",", "").replaceAll("-", "0")));
			recent.put(lane,
					row == null ? 0 : Integer.parseInt(row.child(4).text().replace(",", "").replace("-", "0")));
		}
		builder.matchLane(all);
		builder.matchLane6Months(recent);
	}
}

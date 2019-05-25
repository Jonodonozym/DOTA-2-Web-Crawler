
package jdz.D2WC.dataIO.fetch.dotabuff;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jdz.D2WC.entity.match.Match;
import jdz.D2WC.entity.match.PlayerMatchStats;
import jdz.D2WC.fetch.dotabuff.MatchFetcher;

public class MatchTest {

	@Test
	public void testProMatch() throws IOException {
		Match match = new MatchFetcher().getMatchData(4757225229L);

		assertEquals(21, match.getRadiantScore());
		assertEquals(26, match.getDireScore());
		assertEquals(1569, match.getTimeSeconds());
		assertEquals(10, match.getPlayerStats().size());

		PlayerMatchStats stats = match.getPlayerStats(19672354);
		assertEquals(3, stats.getKills());
		assertEquals(5, stats.getDeaths());
		assertEquals(12, stats.getAssists());
		assertEquals(3400, stats.getGoldWorth());
		assertEquals(7800, stats.getGoldEarned());
	}

	@Test
	public void testBadData() throws IOException {
		Match match = new MatchFetcher().getMatchData(4773522729L);

		assertEquals(51, match.getRadiantScore());
		assertEquals(66, match.getDireScore());
		assertEquals(3222, match.getTimeSeconds());
		assertEquals(10, match.getPlayerStats().size());

		PlayerMatchStats stats = match.getPlayerStats(230780066);
		assertEquals(3, stats.getKills());
		assertEquals(11, stats.getDeaths());
		assertEquals(19, stats.getAssists());
		assertEquals(16700, stats.getGoldWorth());
		assertEquals(19800, stats.getGoldEarned());
	}

}

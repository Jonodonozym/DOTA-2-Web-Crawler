
package jdz.D2WC.fetch.opendota;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import jdz.D2WC.entity.enums.Lane;
import jdz.D2WC.entity.matchStats.PlayerMatchStats;
import jdz.D2WC.fetch.interfaces.PlayerMatchStatsFetcher;
import jdz.D2WC.fetch.opendota.PlayerMatchStatsOpenDota;

public class PlayerMatchStatsTest {
	private final PlayerMatchStatsFetcher fetcher = new PlayerMatchStatsOpenDota();

	@Test
	public void checkMatchHistory() throws IOException {
		int playerID = 383182690; // https://api.opendota.com/api/players/383182690

		List<PlayerMatchStats> matches = fetcher.forPlayerID(playerID);
		assertFalse(matches.isEmpty());
		assertValuesUnique(matches);

		assertOrderedByTimeDesc(matches);
		assertStatsEqual(new PlayerMatchStats(null, -1, 44, Lane.UNKNOWN, false, 16, 11, 23, 1501674951),
				matches.get(matches.size() - 1));
	}

	private void assertValuesUnique(List<?> values) {
		assertEquals(values.size(), new HashSet<>(values).size());
	}

	private void assertOrderedByTimeDesc(List<PlayerMatchStats> matches) {
		long lastTime = Long.MAX_VALUE;
		for (PlayerMatchStats stats: matches) {
			assertTrue(stats.getStartTime() < lastTime);
			lastTime = stats.getStartTime();
		}
	}

	private void assertStatsEqual(PlayerMatchStats expected, PlayerMatchStats actual) {
		assertEquals(expected.getHeroID(), actual.getHeroID());
		assertEquals(expected.getLane(), actual.getLane());
		assertEquals(expected.isRadiantWin(), actual.isRadiantWin());
		assertEquals(expected.getKills(), actual.getKills());
		assertEquals(expected.getDeaths(), actual.getDeaths());
		assertEquals(expected.getAssists(), actual.getAssists());
		assertEquals(expected.getStartTime(), actual.getStartTime());
	}
}


package jdz.D2WC.dataIO.fetch.dotabuff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import jdz.D2WC.entity.matchStats.PlayerMatchStats;
import jdz.D2WC.fetch.opendota.PlayerMatchStatsOpenDota;

public class PlayerMatchHistoryTest {
	@Test
	public void checkMatchHistory() throws IOException {
		List<PlayerMatchStats> matches = new PlayerMatchStatsOpenDota().forPlayerID(383182690L);
		assertTrue(matches.size() > 300);
		checkValuesUnique(matches);
	}
	
	private void checkValuesUnique(List<?> values) {
		assertEquals(values.size(), new HashSet<>(values).size());
	}
}

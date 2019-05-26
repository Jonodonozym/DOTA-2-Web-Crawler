
package jdz.D2WC.dataIO.fetch.dotabuff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import jdz.D2WC.fetch.dotabuff.PlayerMatchHistoryDotaBuff;

public class PlayerMatchHistoryTest {
	@Test
	public void checkMatchHistory() throws IOException {
		List<Long> matches = new PlayerMatchHistoryDotaBuff().getMatchIDs(383182690);
		assertTrue(matches.size() > 400);
		checkValuesUnique(matches);
	}
	
	private void checkValuesUnique(List<Long> values) {
		assertEquals(values.size(), new HashSet<>(values).size());
	}
}

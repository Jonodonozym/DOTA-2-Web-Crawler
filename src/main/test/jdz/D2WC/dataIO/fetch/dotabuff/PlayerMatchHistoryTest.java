
package jdz.D2WC.dataIO.fetch.dotabuff;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import jdz.D2WC.fetch.dotabuff.PlayerMatchHistory;

public class PlayerMatchHistoryTest {

	@Test
	public void checkMatchHistory() throws IOException {
		assertTrue(new PlayerMatchHistory().getMatchIDs(383182690).size() > 400);
	}
}

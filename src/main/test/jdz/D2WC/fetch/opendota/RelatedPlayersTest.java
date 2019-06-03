
package jdz.D2WC.fetch.opendota;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import jdz.D2WC.fetch.opendota.RelatedPlayersOpenDota;

public class RelatedPlayersTest {
	@Test
	public void checkRelatedPlayers() throws IOException {
		// https://www.dotabuff.com/players/383182690
		assertRelatedTo(383182690, 211894829, 137339934, 418441554);
	}

	private void assertRelatedTo(long playerID, long... relatedTo) throws IOException {
		Set<Long> players = new RelatedPlayersOpenDota().getRelatedPlayers(playerID);
		for (long player : relatedTo)
			assertTrue(players.contains(player));
	}

}

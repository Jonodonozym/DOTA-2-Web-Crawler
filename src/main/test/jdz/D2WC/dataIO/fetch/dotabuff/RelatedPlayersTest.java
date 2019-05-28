
package jdz.D2WC.dataIO.fetch.dotabuff;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import jdz.D2WC.fetch.opendota.RelatedPlayersOpenDota;

public class RelatedPlayersTest {
	@Test
	public void checkRelatedPlayers() throws IOException {
		Set<Long> players = new RelatedPlayersOpenDota().getRelatedPlayers(383182690L);
		assertTrue(players.size() > 900);
	}

}

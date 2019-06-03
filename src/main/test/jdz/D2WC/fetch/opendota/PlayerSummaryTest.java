
package jdz.D2WC.fetch.opendota;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import jdz.D2WC.entity.player.PlayerSummary;
import jdz.D2WC.fetch.interfaces.PlayerSummaryFetcher;
import jdz.D2WC.fetch.opendota.PlayerSummaryOpenDota;

public class PlayerSummaryTest {
	private final PlayerSummaryFetcher fetcher = new PlayerSummaryOpenDota();

	@Test
	public void testPlayerFetch() throws IOException {
		assertFetchedSameOrNewerThan(new PlayerSummary(383182690, 2151, 0, 141, 161, false));
		assertFetchedSameOrNewerThan(new PlayerSummary(154715080, 7696, 0, 3386, 2043, false));
	}

	private void assertFetchedSameOrNewerThan(PlayerSummary summary) throws IOException {
		PlayerSummary fetched = fetcher.fromPlayerID(summary.getPlayerID());

		assertEquals(summary.getPlayerID(), fetched.getPlayerID());
		assertThat(fetched.getGamesWon(), greaterThanOrEqualTo(summary.getGamesWon()));
		assertThat(fetched.getGamesLost(), greaterThanOrEqualTo(summary.getGamesLost()));
		assertThat((double) fetched.getMMR(), closeTo(summary.getMMR(), 2000));
		
		assertFalse(fetched.isMatchPlayerFetched());
	}
}

package jdz.D2WC.fetch.opendota;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

import org.junit.Test;

import jdz.D2WC.entity.player.PlayerSummary;
import jdz.D2WC.fetch.interfaces.RandomPlayerFetcher;
import jdz.D2WC.fetch.opendota.PlayerMatchStatsOpenDota;
import jdz.D2WC.fetch.opendota.PlayerSummaryOpenDota;
import jdz.D2WC.fetch.opendota.RandomPlayerOpenDota;

public class RandomPlayerTest {
	private final RandomPlayerFetcher fetcher = new RandomPlayerOpenDota(new PlayerMatchStatsOpenDota(),
			new PlayerSummaryOpenDota());

	@Test
	public void isPlayerFetched() throws IOException {
		PlayerSummary player = fetcher.getSuitableRandom((p) -> true);
		assertTrue(player.getPlayerID() != 0 && player.getPlayerID() != 1);
		assertThat(player.getGames(), greaterThan(0));
		assertThat(player.getMMR(), greaterThan(0));
		assertFalse(player.isMatchPlayerFetched());
	}

	@Test
	public void isPlayerRandom() throws IOException {
		final int maxStrikes = 2, attempts = 5;
		int strikes = 0;

		PlayerSummary player = fetcher.getSuitableRandom((p) -> true);
		for (int i = 0; i < attempts; i++)
			if (fetcher.getSuitableRandom((p) -> true).equals(player))
				if (++strikes == maxStrikes)
					fail("Fetched the same player " + strikes + " out of " + (i + 1) + " times");
	}

	@Test
	public void isPlayerSuitable() throws IOException {
		for (int i = 0; i < 5; i++) {
			PlayerSummary player = fetcher.getSuitableRandom(this::isSuitable);
			assertTrue(isSuitable(player));
		}
	}

	public boolean isSuitable(PlayerSummary summary) throws IOException {
		return summary.getGames() > 4000;
	}

}

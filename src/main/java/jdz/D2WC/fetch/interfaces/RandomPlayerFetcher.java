
package jdz.D2WC.fetch.interfaces;

import java.io.IOException;

import jdz.D2WC.entity.player.PlayerSummary;

public interface RandomPlayerFetcher {
	public static interface SuitabilityTest {
		public boolean test(PlayerSummary summary) throws IOException;
	}
	public PlayerSummary getSuitableRandom(SuitabilityTest test) throws IOException;
}
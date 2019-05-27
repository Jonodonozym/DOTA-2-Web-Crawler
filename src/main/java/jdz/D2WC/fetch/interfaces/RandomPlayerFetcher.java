
package jdz.D2WC.fetch.interfaces;

import java.io.IOException;

import jdz.D2WC.entity.player.PlayerSummary;

public interface RandomPlayerFetcher {
	public PlayerSummary getRandom() throws IOException;
}
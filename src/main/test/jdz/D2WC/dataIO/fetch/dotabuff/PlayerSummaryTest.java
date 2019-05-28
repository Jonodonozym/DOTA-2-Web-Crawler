
package jdz.D2WC.dataIO.fetch.dotabuff;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jdz.D2WC.entity.player.PlayerSummary;
import jdz.D2WC.fetch.opendota.PlayerSummaryOpenDota;

public class PlayerSummaryTest {

	@Test
	public void testPlayerFetch() throws IOException {
		long playerid = 154715080;
		PlayerSummary player = new PlayerSummaryOpenDota().fromPlayerID(playerid);
		assertEquals(playerid, player.getPlayerID());
	}

	@Test
	public void testIncompletePlayerFetch() throws IOException {
		long playerid = 154715080;
		PlayerSummary player = new PlayerSummaryOpenDota().fromPlayerID(playerid);
		assertEquals(playerid, player.getPlayerID());
	}
}


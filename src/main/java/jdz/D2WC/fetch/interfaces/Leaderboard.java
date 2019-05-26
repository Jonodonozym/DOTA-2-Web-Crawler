
package jdz.D2WC.fetch.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface Leaderboard {
	public static interface PlayerIDRunnable {
		public void run(long playerID);
	}

	public void forEachPlayerByMMR(PlayerIDRunnable runnable) throws IOException;

	public default List<Long> getTopPlayersByMMR() throws IOException {
		List<Long> players = new ArrayList<>();
		forEachPlayerByMMR((player) -> players.add(player));
		return players;
	}
}
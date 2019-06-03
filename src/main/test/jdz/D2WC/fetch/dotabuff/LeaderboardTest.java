
package jdz.D2WC.fetch.dotabuff;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import jdz.D2WC.fetch.dotabuff.LeaderboardDotaBuff;
import jdz.D2WC.fetch.interfaces.Leaderboard;

@Ignore
public class LeaderboardTest {
	private final Leaderboard leaderboard = new LeaderboardDotaBuff();

	@Test
	public void test() throws IOException {
		List<Long> players = leaderboard.getTopPlayersByMMR();
		assertThat(players.size(), greaterThan(1500));
	}

}

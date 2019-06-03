
package jdz.D2WC.tasks;

import org.springframework.stereotype.Component;

@Component
public class LeaderboardTask extends AbstractTask {
	public void fetchLeaderboardData() {
		logger.info("Fetching Leaderboard Network");
		fetchHeroData();
		fetchTopPlayerData();
	}

	private void fetchTopPlayerData() {
		long time = System.currentTimeMillis();
		repeatUntilNoError(() -> leaderboard.forEachPlayerByMMR((playerID) -> {
			if (!playerSummaryRepo.existsById(playerID)) {
				fetchMatchData(playerID);
				fetchPlayerSummary(playerID);
			}
		}));
		logger.info(String.format("downloaded summaries for top players (%d ms)", System.currentTimeMillis() - time));
	}
}


package jdz.D2WC.tasks;

import org.springframework.stereotype.Component;

@Component
public class LeaderboardTask extends AbstractTask{
	public void fetchLeaderboardData() {
		logger.info("Fetching Leaderboard Network");
		fetchHeroData();
		fetchTopPlayerSummaries();
		fetchPlayerSummaryAndMatches(getUnfetchedPlayersSummariesFromMatchStatsRepo(), 0);
	}

	private void fetchTopPlayerSummaries() {
		long time = System.currentTimeMillis();
		repeatUntilNoError(() -> leaderboard.forEachPlayerByMMR((playerID) -> {
			if (!playerSummaryRepo.existsById(playerID))
				repeatUntilNoError(() -> {
					playerSummaryRepo.save(playerFetcher.fromPlayerID(playerID));
					playerSummaryRepo.flush();
				});
		}));
		logger.info(String.format("downloaded summaries for top players (%d ms)", System.currentTimeMillis() - time));

	}
}


package jdz.D2WC;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jdz.D2WC.entity.hero.HeroRepository;
import jdz.D2WC.entity.matchStats.MatchStatsRepository;
import jdz.D2WC.entity.matchStats.PlayerMatchStats;
import jdz.D2WC.entity.player.PlayerRepository;
import jdz.D2WC.entity.player.PlayerSummary;
import jdz.D2WC.fetch.dotabuff.LeaderboardDotaBuff;
import jdz.D2WC.fetch.dotabuff.PlayerSummaryDotaBuff;
import jdz.D2WC.fetch.interfaces.HeroesFetcher;
import jdz.D2WC.fetch.interfaces.Leaderboard;
import jdz.D2WC.fetch.interfaces.PlayerMatchStatsFetcher;
import jdz.D2WC.fetch.interfaces.PlayerSummaryFetcher;
import jdz.D2WC.fetch.opendota.HeroesOpenDota;
import jdz.D2WC.fetch.opendota.PlayerMatchStatsOpenDota;

@Component
public class WebCrawlerTask {
	@Autowired private HeroRepository heroRepo;
	@Autowired private PlayerRepository playerSummaryRepo;
	@Autowired private MatchStatsRepository matchStatsRepo;

	@Value("${skip-leaderboard}") private boolean skipLeaderboard;

	private final HeroesFetcher heroesFetcher = new HeroesOpenDota();
	private final Leaderboard leaderboard = new LeaderboardDotaBuff();
	private final PlayerSummaryFetcher playerFetcher = new PlayerSummaryDotaBuff();
	private final PlayerMatchStatsFetcher matchStatsFetcher = new PlayerMatchStatsOpenDota();

	private final Logger logger = LoggerFactory.getLogger("DOTA2");

	public void runTask() {
		if (!skipLeaderboard) {
			fetchHeroData();
			fetchTopPlayerSummaries();
		}

		while (true)
			repeatUntilNoError(() -> {
				fetchPlayerData(getUnfetchedPlayersFromMatchData());
			});
	}

	private void fetchHeroData() {
		repeatUntilNoError(() -> {
			heroRepo.saveAll(heroesFetcher.getAllHeroes());
			heroRepo.flush();
		});
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

	private void fetchPlayerData(Collection<Long> playerIDs) {
		long time = System.currentTimeMillis();
		for (Long playerID : playerIDs)
			repeatUntilNoError(() -> {
				PlayerSummary playerSummary = playerFetcher.fromPlayerID(playerID);
				List<PlayerMatchStats> stats = matchStatsFetcher.forPlayerID(playerID, -1);
				matchStatsRepo.saveAll(stats);
				matchStatsRepo.flush();
				playerSummaryRepo.save(playerSummary);
				playerSummaryRepo.flush();
			});
		logger.info(String.format("downloaded data for %d players (%d ms)", playerIDs.size(),
				System.currentTimeMillis() - time));
	}

	private Collection<Long> getUnfetchedPlayersFromMatchData() {
		Set<Long> allPlayers = matchStatsRepo.getAllPlayerIDs();
		allPlayers.removeAll(playerSummaryRepo.getAllPlayerIDs());
		return allPlayers;
	}

	private void repeatUntilNoError(RunnableError... r) {
		int success = 0;
		while (success < r.length) {
			try {
				r[success].run();
				success++;
			}
			catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(500);
				}
				catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private interface RunnableError {
		public void run() throws Exception;
	}

}

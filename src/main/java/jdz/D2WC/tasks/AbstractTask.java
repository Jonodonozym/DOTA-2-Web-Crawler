
package jdz.D2WC.tasks;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jdz.D2WC.entity.hero.HeroRepository;
import jdz.D2WC.entity.matchStats.MatchStatsRepository;
import jdz.D2WC.entity.player.PlayerRepository;
import jdz.D2WC.fetch.dotabuff.LeaderboardDotaBuff;
import jdz.D2WC.fetch.interfaces.HeroesFetcher;
import jdz.D2WC.fetch.interfaces.Leaderboard;
import jdz.D2WC.fetch.interfaces.PlayerMatchStatsFetcher;
import jdz.D2WC.fetch.interfaces.PlayerSummaryFetcher;
import jdz.D2WC.fetch.opendota.HeroesOpenDota;
import jdz.D2WC.fetch.opendota.PlayerMatchStatsOpenDota;
import jdz.D2WC.fetch.opendota.PlayerSummaryOpenDota;

public class AbstractTask {
	@Autowired protected HeroRepository heroRepo;
	@Autowired protected PlayerRepository playerSummaryRepo;
	@Autowired protected MatchStatsRepository matchStatsRepo;

	protected final HeroesFetcher heroesFetcher = new HeroesOpenDota();
	protected final Leaderboard leaderboard = new LeaderboardDotaBuff();
	protected final PlayerSummaryFetcher playerFetcher = new PlayerSummaryOpenDota();
	protected final PlayerMatchStatsFetcher matchStatsFetcher = new PlayerMatchStatsOpenDota();

	protected final Logger logger = LoggerFactory.getLogger("DOTA2");

	protected void fetchHeroData() {
		repeatUntilNoError(() -> {
			heroRepo.saveAll(heroesFetcher.getAllHeroes());
			heroRepo.flush();
		});
	}

	protected void repeatUntilNoError(RunnableError... r) {
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

	protected interface RunnableError {
		public void run() throws IOException;
	}

	protected void fetchPlayerSummaryAndMatches(Collection<Long> playerIDs) {
		long time = System.currentTimeMillis();
		for (Long playerID : playerIDs) {
			fetchPlayerSummary(playerID);
			fetchMatchData(playerID);
		}
		logger.info(String.format("downloaded data for %d players (%d ms)", playerIDs.size(),
				System.currentTimeMillis() - time));
	}

	protected void fetchPlayerSummary(long playerID) {
		repeatUntilNoError(() -> {
			playerSummaryRepo.save(playerFetcher.fromPlayerID(playerID));
			playerSummaryRepo.flush();
		});
	}

	protected void fetchMatchData(long playerID) {
		repeatUntilNoError(() -> {
			matchStatsRepo.saveAll(matchStatsFetcher.forPlayerID(playerID));
			matchStatsRepo.flush();
		});
	}

	protected Collection<Long> getUnfetchedPlayersSummariesFromMatchStatsRepo() {
		Set<Long> allPlayers = matchStatsRepo.getAllPlayerIDs();
		allPlayers.removeAll(playerSummaryRepo.getPlayerIDs());
		return allPlayers;
	}
}
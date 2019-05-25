
package jdz.D2WC;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jdz.D2WC.entity.hero.Hero;
import jdz.D2WC.entity.hero.HeroRepository;
import jdz.D2WC.entity.match.MatchRepository;
import jdz.D2WC.entity.player.PlayerRepository;
import jdz.D2WC.entity.player.PlayerSummary;
import jdz.D2WC.fetch.dotabuff.HerosFetcher;
import jdz.D2WC.fetch.dotabuff.MatchFetcher;
import jdz.D2WC.fetch.dotabuff.PlayerFetcher;
import jdz.D2WC.fetch.dotabuff.PlayerMatchHistory;
import jdz.D2WC.fetch.dotabuff.TopPlayersList;

@Component
public class WebCrawlerTask {
	@Autowired private HeroRepository heroRepo;
	@Autowired private PlayerRepository playerSummaryRepo;
	@Autowired private MatchRepository matchRepo;

	private final HerosFetcher heroesFetcher = new HerosFetcher();
	private final TopPlayersList topPlayersFetcher = new TopPlayersList();
	private final PlayerFetcher playerFetcher = new PlayerFetcher();
	private final PlayerMatchHistory playerMatchHistoryFetcher = new PlayerMatchHistory();
	private final MatchFetcher matchFetcher = new MatchFetcher();

	private final Logger logger = LoggerFactory.getLogger("DOTA2");

	public void runTask() {

		fetchHeroData();
		fetchTopPlayerSummaries();

		while (true)
			repeatUntilNoError(() -> {
				downloadPlayerMatches(playerSummaryRepo.getAllPlayerIDWhereMatchesNotFetched());
				fetchPlayerSummaries(getUnfetchedPlayersFromMatchData());
			});
	}

	private void fetchHeroData() {
		repeatUntilNoError(() -> {
			List<String> heroNames = heroesFetcher.getHeroNames();
			Set<Hero> dbHeroes = new HashSet<>(heroRepo.findAll());
			heroNames.removeIf((e) -> dbHeroes.contains(new Hero(e, null)));
			heroRepo.saveAll(heroesFetcher.getAllHeroes(heroNames));
			heroRepo.flush();
		});
	}

	private void fetchTopPlayerSummaries() {
		long time = System.currentTimeMillis();
		repeatUntilNoError(() -> topPlayersFetcher.forEachPlayerID((playerID) -> {
			if (!playerSummaryRepo.existsById(playerID))
				repeatUntilNoError(() -> {
					playerSummaryRepo.save(playerFetcher.fetchSummary(playerID));
					playerSummaryRepo.flush();
				});
		}));
		logger.info(String.format("downloaded summaries for top players (%d ms)",System.currentTimeMillis()-time));

	}

	private void fetchPlayerSummaries(Collection<Long> playerIDs) {
		long time = System.currentTimeMillis();
		for (Long playerID : playerIDs)
			repeatUntilNoError(() -> {
				playerSummaryRepo.save(playerFetcher.fetchSummary(playerID));
				playerSummaryRepo.flush();
			});
		logger.info(String.format("downloaded summaries for %d players (%d ms)", playerIDs.size(),
				System.currentTimeMillis() - time));
	}

	private void downloadPlayerMatches(Collection<Long> playerIDs) {
		for (long playerID : playerIDs)
			repeatUntilNoError(() -> {
				long time = System.currentTimeMillis();
				List<Long> matches = playerMatchHistoryFetcher.getMatchIDs(playerID);
				downloadMatches(matches);

				PlayerSummary summary = playerSummaryRepo.getOne(playerID);
				summary.setFetchedMatches(true);
				playerSummaryRepo.save(summary);
				playerSummaryRepo.flush();

				logger.info(String.format("downloaded %d matches for player %d (%d ms)", matches.size(), playerID,
						System.currentTimeMillis() - time));
			});
	}

	private void downloadMatches(Collection<Long> matchIDs) {
		for (long matchID : matchIDs)
			if (!matchRepo.existsById(matchID))
				repeatUntilNoError(() -> {
					matchRepo.save(matchFetcher.getMatchData(matchID));
					matchRepo.flush();
				});
	}

	private Collection<Long> getUnfetchedPlayersFromMatchData() {
		Set<Long> allPlayers = matchRepo.getAllPlayerIDs();
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

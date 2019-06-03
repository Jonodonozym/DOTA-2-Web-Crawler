
package jdz.D2WC.tasks;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.springframework.stereotype.Component;

import jdz.D2WC.entity.player.PlayerSummary;
import jdz.D2WC.fetch.interfaces.RandomPlayerFetcher;
import jdz.D2WC.fetch.interfaces.RelatedPlayersFetcher;
import jdz.D2WC.fetch.opendota.RandomPlayerOpenDota;
import jdz.D2WC.fetch.opendota.RelatedPlayersOpenDota;

@Component
public class RecursiveTask extends AbstractTask {
	private final RelatedPlayersFetcher relatedPlayers = new RelatedPlayersOpenDota();
	private final RandomPlayerFetcher randomPlayer = new RandomPlayerOpenDota(matchStatsFetcher, playerFetcher);

	public void fetchPlayerData(int depth) {
		if (depth == -1)
			depth = Integer.MAX_VALUE;

		logger.info("Fetching Random Player Network");

		fetchHeroData();

		if (playerSummaryRepo.count() == 0) {
			repeatUntilNoError(() -> {
				long player = randomPlayer.getSuitableRandom(this::isSuitableRootPlayer).getPlayerID();
				fetchPlayerSummaryAndMatches(Arrays.asList(player));
				logger.info("Fetched random player with id=" + player + " as network root");
			});
		}

		for (int i = 0; i < depth; i++) {
			logger.info("Performing recursive network search, depth = " + (i + 1) + "/" + depth + ", "
					+ playerSummaryRepo.count() + " players in database");
			performRecursiveFetch();
		}
	}

	private boolean isSuitableRootPlayer(PlayerSummary player) throws IOException {
		return player.getGames() > 1000 && player.getMMR() > 1800 && player.getMMR() < 3800
				&& relatedPlayers.getRelatedPlayers(player.getPlayerID()).size() > 100;
	}

	private void performRecursiveFetch() {
		Set<Long> players = playerSummaryRepo.getPlayerIDsWhereNetworkUnfetched();
		for (long playerID : players)
			fetchNetworkSummariesFromHistory(playerID);
	}

	private void fetchNetworkSummariesFromHistory(long playerID) {
		repeatUntilNoError(() -> {
			Set<Long> players = relatedPlayers.getRelatedPlayers(playerID);
			players.removeIf((id) -> playerSummaryRepo.existsById(id));
			fetchPlayerSummaryAndMatches(players);

			PlayerSummary summary = playerSummaryRepo.getOne(playerID);
			summary.setMatchPlayerFetched(true);
			playerSummaryRepo.save(summary);
		});
	}

}


package jdz.D2WC.tasks;

import java.util.Arrays;
import java.util.Set;

import org.springframework.stereotype.Component;

import jdz.D2WC.fetch.interfaces.RandomPlayerFetcher;
import jdz.D2WC.fetch.opendota.RandomPlayerOpenDota;

@Component
public class RecursiveTask extends AbstractTask {
	private final RandomPlayerFetcher randomPlayer = new RandomPlayerOpenDota(matchStatsFetcher, playerFetcher);
	private static final int MAX_MATCH_HISTORY = 100;

	public void fetchPlayerData() {
		fetchHeroData();

		if (playerSummaryRepo.count() == 0) {
			repeatUntilNoError(() -> {
				long player = randomPlayer.getRandom().getPlayerID();
				fetchPlayerSummaryAndMatches(Arrays.asList(player));
			});
		}

		while (true)
			performRecursiveFetch();
	}

	private void performRecursiveFetch() {
		fetchPlayerSummaryAndMatches(getUnfetchedPlayersSummariesFromMatchStatsRepo());
		Set<Long> players = playerSummaryRepo.getPlayerIDsWhereNetworkUnfetched();
		fetchPlayerSummaryAndMatches(players);
		for (long playerID : players)
			fetchNetworkSummariesFromHistory(playerID);
	}

	private void fetchNetworkSummariesFromHistory(long playerID) {
		int history = MAX_MATCH_HISTORY;
		for (long matchID : matchStatsRepo.getMatchIDs(playerID)) {
			if (matchStatsRepo.countEntriesForMatch(matchID) >= 10)
				continue;
			if (history-- <= 0)
				return;
			repeatUntilNoError(() -> matchStatsRepo.saveAll(matchStatsFetcher.forMatchID(matchID)));
			matchStatsRepo.flush();
		}
	}

}

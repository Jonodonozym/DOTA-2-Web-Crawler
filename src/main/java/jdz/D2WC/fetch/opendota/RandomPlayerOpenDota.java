
package jdz.D2WC.fetch.opendota;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import jdz.D2WC.entity.matchStats.PlayerMatchStats;
import jdz.D2WC.entity.player.PlayerSummary;
import jdz.D2WC.fetch.interfaces.PlayerMatchStatsFetcher;
import jdz.D2WC.fetch.interfaces.PlayerSummaryFetcher;
import jdz.D2WC.fetch.interfaces.RandomPlayerFetcher;
import jdz.D2WC.fetch.util.JSONListDataParser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RandomPlayerOpenDota extends JSONListDataParser.RootArray<Long> implements RandomPlayerFetcher {
	private final PlayerMatchStatsFetcher matchFetcher;
	private final PlayerSummaryFetcher summaryFetcher;

	@Override
	public PlayerSummary getSuitableRandom(SuitabilityTest test) throws IOException {
		List<Long> matches = getAll();
		Collections.shuffle(matches);
		for (long matchID : matches) {
			List<PlayerMatchStats> players = matchFetcher.forMatchID(matchID);
			players.removeIf((p) -> p.getPlayerID() <= 0);

			if (players.size() == 0)
				continue;

			PlayerMatchStats randomPlayer = players.get(new Random().nextInt(players.size()));
			PlayerSummary summary = summaryFetcher.fromPlayerID(randomPlayer.getPlayerID());
			if (test.test(summary))
				return summary;
		}
		return getSuitableRandom(test);
	}

	@Override
	protected String getPage(Object... state) {
		return OpenDotaAPI.URL() + "publicMatches";
	}

	@Override
	protected Long parseRow(JSONObject object) {
		return object.getLong("match_id");
	}
}
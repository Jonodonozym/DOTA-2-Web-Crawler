
package jdz.D2WC.fetch.opendota;

import java.io.IOException;
import java.util.List;

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
	public PlayerSummary getRandom() throws IOException {
		List<Long> matches = getAll();
		for (long matchID: matches) {
			for (PlayerMatchStats stats: matchFetcher.forMatchID(matchID)) {
				if (stats.getPlayerID() <= 0)
					continue;
				PlayerSummary summary = summaryFetcher.fromPlayerID(stats.getPlayerID());
				if (isSuitableRootPlayer(summary))
					return summary;
			}
		}
		return getRandom();
	}

	private boolean isSuitableRootPlayer(PlayerSummary player) {
		return player.getPlayerID() > 0 && player.getGames() > 1000 && player.getMMR() > 1800 && player.getMMR() < 3800;
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
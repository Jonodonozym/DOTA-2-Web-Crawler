
package jdz.D2WC.fetch.opendota;

import java.io.IOException;

import org.json.JSONObject;

import javafx.util.Pair;
import jdz.D2WC.entity.player.PlayerSummary;
import jdz.D2WC.fetch.interfaces.PlayerSummaryFetcher;
import jdz.D2WC.fetch.util.JSONParser;

public class PlayerSummaryOpenDota extends JSONParser<JSONObject> implements PlayerSummaryFetcher {

	@Override
	public PlayerSummary fromPlayerID(long playerID) throws IOException {
		JSONObject MMREstimate = readJSONFromUrl(OpenDotaAPI.URL() + "players/" + playerID)
				.getJSONObject("mmr_estimate");
		if (!MMREstimate.has("estimate"))
			return new PlayerSummary(playerID, 0, 0, 0, 0, false);
		Pair<Integer, Integer> winLoss = getWinLoss(playerID);
		return new PlayerSummary(playerID, MMREstimate.getInt("estimate"),
				MMREstimate.has("stdDev") ? MMREstimate.getInt("stdDev") : 0, winLoss.getKey(), winLoss.getValue(),
				false);
	}

	private Pair<Integer, Integer> getWinLoss(long playerID) throws IOException {
		JSONObject object = readJSONFromUrl(OpenDotaAPI.URL() + "players/" + playerID + "/wl");
		return new Pair<Integer, Integer>(object.getInt("win"), object.getInt("lose"));
	}

	@Override
	protected JSONObject parse(String source) {
		return new JSONObject(source);
	}

}

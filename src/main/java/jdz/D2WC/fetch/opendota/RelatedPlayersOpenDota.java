
package jdz.D2WC.fetch.opendota;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import jdz.D2WC.fetch.interfaces.RelatedPlayersFetcher;
import jdz.D2WC.fetch.util.JSONListDataParser;
import jdz.D2WC.fetch.util.RateLimiter;

public class RelatedPlayersOpenDota extends JSONListDataParser.RootArray<List<Long>> implements RelatedPlayersFetcher {
	@Override
	public Set<Long> getRelatedPlayers(long playerID) throws IOException {
		Set<Long> players = new HashSet<>();
		for (List<Long> sublist : getAll(playerID))
			players.addAll(sublist);
		players.remove(playerID);
		if (players.size() > 500)
			RateLimiter.waitIfTooFast(5000);
		return players;
	}

	@Override
	protected String getPage(Object... state) {
		return OpenDotaAPI.URL() + "players/" + state[0] + "/matches?project=heroes";
	}

	@Override
	protected List<Long> parseRow(JSONObject object) {
		List<Long> players = new ArrayList<>();
		for (String key : object.getJSONObject("heroes").keySet()) {
			JSONObject hero = object.getJSONObject("heroes").getJSONObject(key);
			if (hero.has("account_id") && hero.get("account_id") instanceof Number)
				players.add(hero.getLong("account_id"));
		}
		return players;
	}

}

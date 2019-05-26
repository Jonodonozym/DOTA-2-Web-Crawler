
package jdz.D2WC.fetch.opendota;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import jdz.D2WC.entity.enums.Lane;
import jdz.D2WC.entity.matchStats.PlayerMatchStats;
import jdz.D2WC.entity.matchStats.PlayerMatchStats.MatchStatsID;
import jdz.D2WC.fetch.interfaces.PlayerMatchStatsFetcher;
import jdz.D2WC.fetch.util.JSONListDataParser;

public class PlayerMatchStatsOpenDota implements PlayerMatchStatsFetcher {
	private final PlayerParser playerParser = new PlayerParser();
	private final MatchParser matchParser = new MatchParser();

	@Override
	public List<PlayerMatchStats> forPlayerID(long playerID, int limit) throws IOException {
		return playerParser.getAll(playerID, limit);
	}

	@Override
	public List<PlayerMatchStats> forMatchID(long matchID) throws IOException {
		return matchParser.getAll(matchID);
	}

	protected PlayerMatchStats parseRow(JSONObject object, int playerID) {
		MatchStatsID id = new MatchStatsID(object.getInt("match_id"), object.getInt("player_slot"));
		return new PlayerMatchStats(id, playerID, object.getInt("hero_id"),
				object.getBoolean("is_roaming?") ? Lane.ROAMING : Lane.values()[object.getInt("lane")],
				object.getBoolean("radiant_win"), object.getInt("kills"), object.getInt("deaths"),
				object.getInt("assists"), object.getLong("start_time"));
	}

	private class PlayerParser extends JSONListDataParser.RootArray<PlayerMatchStats> {
		private Object[] state;

		@Override
		protected String getPage(Object... state) {
			this.state = state;
			String limit = ((int) state[1]) > 0 ? "?limit=" + state[1] : "";
			return OpenDotaAPI.URL + "players/" + state[0] + "/matches" + limit;
		}

		@Override
		protected PlayerMatchStats parseRow(JSONObject object) {
			return PlayerMatchStatsOpenDota.this.parseRow(object, (int) state[0]);
		}
	}

	private class MatchParser extends JSONListDataParser.RootObject<PlayerMatchStats> {
		@Override
		protected String getPage(Object... state) {
			return OpenDotaAPI.URL + "https://api.opendota.com/api/matches/" + state[0];
		}

		@Override
		protected JSONArray selectList(JSONObject root) {
			return root.getJSONArray("players");
		}

		@Override
		protected PlayerMatchStats parseRow(JSONObject object) {
			return PlayerMatchStatsOpenDota.this.parseRow(object, object.getInt("account_id"));
		}
	}
}


package jdz.D2WC.fetch.opendota;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public List<PlayerMatchStats> forPlayerID(long playerID) throws IOException {
		Set<PlayerMatchStats> stats = new HashSet<>();
		for (Lane lane : new Lane[] { Lane.SAFE, Lane.MID, Lane.OFF, Lane.JUNGLE })
			stats.addAll(playerParser.getAll(playerID, lane));
		stats.addAll(playerParser.getAll(playerID));
		return new ArrayList<>(stats);
	}

	@Override
	public List<PlayerMatchStats> forMatchID(long matchID) throws IOException {
		return matchParser.getAll(matchID);
	}

	protected PlayerMatchStats parseRow(JSONObject object, long playerID) {
		boolean roaming = object.has("is_roaming") && object.get("is_roaming").getClass().equals(Boolean.class)
				? object.getBoolean("is_roaming")
				: false;
		Lane lane = roaming ? Lane.ROAMING
				: object.has("lane") && object.get("lane").getClass().equals(Integer.class)
						? Lane.values()[object.getInt("lane")]
						: Lane.UNKNOWN;
		return parseRow(object, playerID, lane);
	}

	protected PlayerMatchStats parseRow(JSONObject object, long playerID, Lane lane) {
		MatchStatsID id = new MatchStatsID(object.getInt("match_id"), object.getInt("player_slot"));
		return new PlayerMatchStats(id, playerID, object.getInt("hero_id"), lane, object.getBoolean("radiant_win"),
				object.getInt("kills"), object.getInt("deaths"), object.getInt("assists"),
				object.getLong("start_time"));
	}

	private class PlayerParser extends JSONListDataParser.RootArray<PlayerMatchStats> {
		private Object[] state;

		@Override
		protected String getPage(Object... state) {
			this.state = state;
			String laneRole = state.length > 1 ? "?lane_role=" + ((Lane) state[1]).ordinal() : "";
			return OpenDotaAPI.URL() + "players/" + state[0] + "/matches" + laneRole;
		}

		@Override
		protected PlayerMatchStats parseRow(JSONObject object) {
			return PlayerMatchStatsOpenDota.this.parseRow(object, (long) state[0],
					state.length > 1 ? (Lane) state[1] : Lane.UNKNOWN);
		}
	}

	private class MatchParser extends JSONListDataParser.RootObject<PlayerMatchStats> {
		@Override
		protected String getPage(Object... state) {
			return OpenDotaAPI.URL() + "matches/" + state[0];
		}

		@Override
		protected JSONArray selectList(JSONObject root) {
			return root.getJSONArray("players");
		}

		@Override
		protected PlayerMatchStats parseRow(JSONObject object) {
			return PlayerMatchStatsOpenDota.this.parseRow(object,
					object.get("account_id").getClass().equals(Integer.class) ? object.getLong("account_id") : -1);
		}
	}
}

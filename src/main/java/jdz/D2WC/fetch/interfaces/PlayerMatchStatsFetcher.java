
package jdz.D2WC.fetch.interfaces;

import java.io.IOException;
import java.util.List;

import jdz.D2WC.entity.matchStats.PlayerMatchStats;

public interface PlayerMatchStatsFetcher {	
	public List<PlayerMatchStats> forPlayerID(long playerID, int limit) throws IOException;
	public List<PlayerMatchStats> forMatchID(long matchID) throws IOException;
}
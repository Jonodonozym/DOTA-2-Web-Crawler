
package jdz.D2WC.fetch.interfaces;

import java.io.IOException;
import java.util.Set;

public interface RelatedPlayersFetcher {
	public Set<Long> getRelatedPlayers(long PlayerID) throws IOException;
}

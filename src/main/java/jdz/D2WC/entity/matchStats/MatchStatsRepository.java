
package jdz.D2WC.entity.matchStats;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchStatsRepository extends JpaRepository<PlayerMatchStats, PlayerMatchStats.MatchStatsID> {
	@Query("select p.id.matchID from #{#entityName} p where p.playerID = ?1 order by p.startTime desc")
	List<Long> getMatchIDs(long playerID);
	
	@Query("select distinct p.playerID from #{#entityName} p")
	Set<Long> getAllPlayerIDs();

}
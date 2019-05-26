
package jdz.D2WC.entity.matchStats;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// TODO
public interface MatchStatsRepository extends JpaRepository<PlayerMatchStats, PlayerMatchStats.MatchStatsID> {
	@Query("select p.id from #{#entityName} p where p.startTime > (select max(p.startTime) from #{#entityName} p) - 7776000")
	Set<PlayerMatchStats.MatchStatsID> getRecentMatchIDs();
	@Query("select distinct p.playerID from #{#entityName} p")
	Set<Long> getAllPlayerIDs();
}
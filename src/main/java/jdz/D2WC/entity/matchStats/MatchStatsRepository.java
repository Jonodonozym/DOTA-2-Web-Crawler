
package jdz.D2WC.entity.matchStats;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchStatsRepository extends JpaRepository<PlayerMatchStats, PlayerMatchStats.MatchStatsID> {
	@Query("select p.id.match_id from #{#entityName} p where p.playerID = #{#pid} order by p.startTime desc")
	List<Long> getMatchIDs(@Param("pid") long playerID);
	@Query("select distinct p.playerID from #{#entityName} p")
	Set<Long> getAllPlayerIDs();

	@Query("select count(*) from #{#entityName} p where p.matchID = #{#mid}")
	int countEntriesForMatch(@Param("mid") long matchID);
}
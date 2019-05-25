
package jdz.D2WC.entity.match;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchRepository extends JpaRepository<Match, Long> {
	@Query("select m.id from #{#entityName} m")
	Set<Long> getAllMatchIDs();
	@Query("select distinct p.playerID from PlayerMatchStats p")
	Set<Long> getAllPlayerIDs();
}
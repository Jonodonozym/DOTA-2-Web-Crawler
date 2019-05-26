
package jdz.D2WC.entity.player;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlayerRepository extends JpaRepository<PlayerSummary, Long> {
	@Query("select p.playerID from #{#entityName} p")
	Set<Long> getAllPlayerIDs();
}

package jdz.D2WC.entity.match;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@EqualsAndHashCode(of = { "id" })
public class Match {
	@Id private long id;
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "playerID")
	private List<PlayerMatchStats> playerStats;
	private int radiantScore;
	private int direScore;
	private int timeSeconds;

	public PlayerMatchStats getPlayerStats(long player) {
		for (PlayerMatchStats stats : playerStats)
			if (stats.getPlayerID() == player)
				return stats;
		return null;
	}

	public List<Long> getRadiantPlayers() {
		return getTeam(true);
	}

	public List<Long> getDirePlayers() {
		return getTeam(false);
	}

	private List<Long> getTeam(boolean radiant) {
		return getPlayerStats().stream().filter((e) -> e.isRadiant() == radiant).map((e) -> e.getPlayerID())
				.collect(Collectors.toList());
	}
}

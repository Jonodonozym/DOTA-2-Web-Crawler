
package jdz.D2WC.entity.matchStats;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import jdz.D2WC.entity.enums.Lane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class PlayerMatchStats implements Serializable {
	private static final long serialVersionUID = 1954775663036659457L;
	
	@EmbeddedId MatchStatsID id;
	private long playerID;
	private int heroID;
	private Lane lane;
	private boolean radiantWin;
	private int kills;
	private int deaths;
	private int assists;
	private long startTime;
	
	@AllArgsConstructor
	@Embeddable
	public static class MatchStatsID {
		@Column(name = "match_id") private long matchID;
		@Column(name = "slot") private int slot;
	}
}
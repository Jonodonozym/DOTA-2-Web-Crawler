
package jdz.D2WC.entity.player;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@ToString
@Builder
public class PlayerSummary {
	@Id private long playerID;
	private int MMR;
	private int mmrStdDev;
	private int gamesWon;
	private int gamesLost;
	@Default private boolean matchPlayerFetched = false;
	
	public int getGames() {
		return gamesWon + gamesLost;
	}
}
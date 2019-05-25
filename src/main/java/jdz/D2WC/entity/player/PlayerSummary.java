
package jdz.D2WC.entity.player;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;

import jdz.D2WC.entity.enums.Lane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
	private int gamesWon;
	private int gamesLost;
	private int gamesWon6Months;
	private int gamesLost6Months;

	@Builder.Default @Setter private boolean fetchedMatches = false;
	
	@ElementCollection
    @MapKeyColumn(name="lane")
	@MapKeyEnumerated(EnumType.ORDINAL)
    @Column(name="matches")
    @CollectionTable(name="laneMatchesAllTime", joinColumns=@JoinColumn(name="playerID"))
	private Map<Lane, Integer> matchLane;

	@ElementCollection
    @MapKeyColumn(name="lane")
	@MapKeyEnumerated(EnumType.ORDINAL)
    @Column(name="matches")
    @CollectionTable(name="laneMatchesSixMonths", joinColumns=@JoinColumn(name="playerID"))
	private Map<Lane, Integer> matchLane6Months;
}

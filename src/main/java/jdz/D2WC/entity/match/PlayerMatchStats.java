
package jdz.D2WC.entity.match;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import jdz.D2WC.entity.enums.Lane;
import jdz.D2WC.entity.hero.Hero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "PlayerMatchStats")
public class PlayerMatchStats {
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private long id;
	private long playerID;
	@OneToOne(cascade = CascadeType.PERSIST) private Hero hero;
	@Enumerated(EnumType.ORDINAL) private Lane lane;
	private boolean radiant;
	private int kills;
	private int deaths;
	private int assists;
	private int goldWorth;
	private int goldEarned;
}

package jdz.D2WC.entity.hero;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@ToString(of = { "name" })
public class Hero {
	@Id private int id;
	private String name;
	@ElementCollection @Enumerated(EnumType.ORDINAL) private List<Role> roles;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Hero))
			return false;
		return ((Hero) o).name.replace('-', ' ').equalsIgnoreCase(name.replace('-', ' '));
	}
	
	@Override
	public int hashCode() {
		return name.replace('-', ' ').toLowerCase().hashCode();
	}
}

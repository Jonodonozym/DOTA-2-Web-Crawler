
package jdz.D2WC.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Lane {
	ROAMING("Roaming"), OFF("Off Lane"), SAFE("Safe Lane"), MID("Mid Lane"), JUNGLE("Jungle"),
	UNKNOWN("Unanalyzed Lane");

	private final String dotabuffLabel;

	public static Lane fromDotaBuffLabel(String label) {
		for (Lane lane : Lane.values())
			if (lane.dotabuffLabel.equals(label))
				return lane;
		throw new IllegalArgumentException("Unkown lane: " + label);
	}
}

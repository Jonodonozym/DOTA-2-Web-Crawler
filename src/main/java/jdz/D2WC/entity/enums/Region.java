
package jdz.D2WC.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Region {
	EU("europe"), AM("americas"), SEA("se_asia"), CN("china");
	
	@Getter private final String longName;
}

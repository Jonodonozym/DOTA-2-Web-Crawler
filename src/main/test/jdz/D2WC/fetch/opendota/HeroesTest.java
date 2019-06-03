
package jdz.D2WC.fetch.opendota;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jdz.D2WC.entity.hero.Hero;
import jdz.D2WC.entity.hero.Role;
import static jdz.D2WC.entity.hero.Role.*;
import jdz.D2WC.fetch.interfaces.HeroesFetcher;
import jdz.D2WC.fetch.opendota.HeroesOpenDota;

public class HeroesTest {
	private final HeroesFetcher fetcher = new HeroesOpenDota();

	@Test
	public void test() throws IOException {
		List<Hero> heroes = fetcher.getAllHeroes();
		assertTrue(!heroes.isEmpty());

		// https://dota2.gamepedia.com/Sven#Gameplay
		assertHasRoles(getHero(heroes, "Sven"), MELEE, CARRY, DISABLER, INITIATOR, DURABLE, NUKER);

		// https://dota2.gamepedia.com/Tidehunter#Gameplay
		assertHasRoles(getHero(heroes, "Tidehunter"), MELEE, INITIATOR, DURABLE, DISABLER, NUKER);
	}

	private Hero getHero(List<Hero> heroes, String name) {
		for (Hero hero : heroes)
			if (hero.getName().equalsIgnoreCase(name))
				return hero;
		fail("No hero found called: " + name);
		return null;
	}

	private void assertHasRoles(Hero hero, Role... roleArray) {
		List<Role> roles = Arrays.asList(roleArray);
		for (Role role : Role.values())
			assertEquals(hero.getRoles().contains(role), roles.contains(role));
	}
}

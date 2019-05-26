
package jdz.D2WC.fetch.interfaces;

import java.io.IOException;
import java.util.List;

import jdz.D2WC.entity.hero.Hero;

public interface HeroesFetcher {
	public List<Hero> getAllHeroes() throws IOException;
}

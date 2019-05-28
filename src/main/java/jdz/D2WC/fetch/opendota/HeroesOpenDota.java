
package jdz.D2WC.fetch.opendota;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import jdz.D2WC.entity.hero.Hero;
import jdz.D2WC.entity.hero.Role;
import jdz.D2WC.fetch.interfaces.HeroesFetcher;
import jdz.D2WC.fetch.util.JSONListDataParser;

public class HeroesOpenDota extends JSONListDataParser.RootArray<Hero> implements HeroesFetcher {
	@Override
	public List<Hero> getAllHeroes() throws IOException {
		return getAll();
	}

	@Override
	protected String getPage(Object... state) {
		return OpenDotaAPI.URL() + "heroes";
	}

	@Override
	protected Hero parseRow(JSONObject object) {
		List<Role> roles = new ArrayList<>();
		roles.add(Role.valueOf(object.getString("attack_type").toUpperCase()));
		for (Object role : object.getJSONArray("roles"))
			roles.add(Role.valueOf(((String)role).toUpperCase()));
		return new Hero(object.getInt("id"), object.getString("localized_name"), roles);
	}


}

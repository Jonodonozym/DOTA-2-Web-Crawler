
package jdz.D2WC.fetch.dotabuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import jdz.D2WC.entity.hero.Hero;
import jdz.D2WC.entity.hero.Role;
import jdz.D2WC.fetch.interfaces.HeroesFetcher;
import jdz.D2WC.fetch.util.HTMLDocumentParser;

public class HeroesDotaBuff extends HTMLDocumentParser implements HeroesFetcher {
	public List<String> getHeroNames() throws IOException {
		List<String> heroes = new ArrayList<>();
		Document document = getDocument("https://www.dotabuff.com/heroes");
		for (Element element : document.selectFirst("div[class='hero-grid']").children().select("a")) {
			String href = element.attr("href");
			heroes.add(href.substring(href.lastIndexOf('/') + 1));
		}
		return heroes;
	}
	
	public List<Hero> getAllHeroes() throws IOException {
		return getAllHeroes(getHeroNames());
	}

	public List<Hero> getAllHeroes(List<String> names) throws IOException {
		List<Hero> heroes = new ArrayList<>();
		for (String name : names)
			heroes.add(parseHeroPage(getDocument("https://www.dotabuff.com/heroes/" + name)));
		return heroes;
	}

	public Hero parseHeroPage(Document document) {
		Element headerContent = document.selectFirst("div[class='header-content-title']").child(0);
		String html = headerContent.html();
		String name = html.substring(0, html.indexOf('<'));
		String roles = html.substring(html.indexOf('>') + 1, html.lastIndexOf('<'));
		return new Hero(-1, name, parseRoles(roles));
	}

	private List<Role> parseRoles(String roleString) {
		List<Role> roles = new ArrayList<>();
		String[] roleStrings = roleString.split(",");
		for (String s : roleStrings)
			roles.add(Role.valueOf(s.trim().toUpperCase()));
		return roles;
	}
}

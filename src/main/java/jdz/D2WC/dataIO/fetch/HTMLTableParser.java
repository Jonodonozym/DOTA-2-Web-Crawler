
package jdz.D2WC.dataIO.fetch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class HTMLTableParser<E> extends HTMLDocumentParser {
	public List<E> getAll(Object... state) throws IOException {
		List<E> elements = new ArrayList<>();

		Document doc = getDocument(getPage(state));

		getTableRows(doc).forEach((row) -> {
			try {
				elements.add(extractFromRow(row.select("td")));
			}
			catch (Exception ex) {}
		});

		return elements;
	}

	protected abstract String getPage(Object... state);

	protected abstract E extractFromRow(Elements columns);

	protected abstract Elements getTableRows(Document document);

	protected String extractLeafText(Elements elements, int index) {
		return extractLeafText(elements.get(index));
	}

	protected String extractLeafText(Element element) {
		if (element.hasText())
			return element.text();
		for (Element child : element.select("")) {
			String s = extractLeafText(child);
			if (s != null)
				return s;
		}
		return null;
	}

	protected double parseDouble(String text) {
		double mult = 1;
		if (text.endsWith("k"))
			mult = 1e3;
		if (text.endsWith("M"))
			mult = 1e6;
		if (text.endsWith("B"))
			mult = 1e9;
		if (text.endsWith("T"))
			mult = 1e12;
		try {
			return Double.parseDouble(text.replaceAll("[^\\d.-]", "")) * mult;
		}
		catch (Exception e) {
			return 0;
		}
	}
}


package jdz.D2WC.fetch.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class JSONListDataParser<E, T> extends JSONParser<T> {
	public static abstract class RootObject<E> extends JSONListDataParser<E, JSONObject> {
		protected JSONObject parse(String source) {
			return new JSONObject(source);
		}
	}
	
	public static abstract class RootArray<E> extends JSONListDataParser<E, JSONArray> {
		protected JSONArray parse(String source) {
			return new JSONArray(source);
		}

		protected JSONArray selectList(JSONArray root) {
			return root;
		}
	}
	
	public List<E> getAll(Object... state) throws IOException {
		List<E> elements = new ArrayList<>();
		JSONArray JSONList = selectList(readJSONFromUrl(getPage(state)));
		JSONList.forEach((object) -> {
			elements.add(parseRow((JSONObject) object));
		});
		return elements;
	}

	protected abstract String getPage(Object... state);

	protected abstract JSONArray selectList(T root);

	protected abstract E parseRow(JSONObject object);
}

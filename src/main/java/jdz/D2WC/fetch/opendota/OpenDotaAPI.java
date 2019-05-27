
package jdz.D2WC.fetch.opendota;

import lombok.Getter;

public class OpenDotaAPI {
	private static final String URL = "https://api.opendota.com/api/";
	@Getter private static int apiCalls = 0;
	
	public static String URL() {
		apiCalls++;
		return URL;
	}
}

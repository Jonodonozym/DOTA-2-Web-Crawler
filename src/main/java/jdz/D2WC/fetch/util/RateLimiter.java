
package jdz.D2WC.fetch.util;

public class RateLimiter {
	private static long lastConnect = System.currentTimeMillis();

	public static void waitIfTooFast(int waitTimeMS) {
		long timeDiff = System.currentTimeMillis() - lastConnect;
		if (timeDiff < waitTimeMS)
			try {
				Thread.sleep(waitTimeMS - timeDiff);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		lastConnect = System.currentTimeMillis();
	}
}

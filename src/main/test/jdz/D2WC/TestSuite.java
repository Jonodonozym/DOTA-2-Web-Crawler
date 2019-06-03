
package jdz.D2WC;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jdz.D2WC.entity.HibernateJPATest;
import jdz.D2WC.fetch.dotabuff.LeaderboardTest;
import jdz.D2WC.fetch.opendota.HeroesTest;
import jdz.D2WC.fetch.opendota.PlayerMatchStatsTest;
import jdz.D2WC.fetch.opendota.PlayerSummaryTest;
import jdz.D2WC.fetch.opendota.RandomPlayerTest;
import jdz.D2WC.fetch.opendota.RelatedPlayersTest;

@RunWith(Suite.class)
@SuiteClasses({ HibernateJPATest.class , HeroesTest.class, PlayerMatchStatsTest.class, PlayerSummaryTest.class,
		RandomPlayerTest.class, RelatedPlayersTest.class, LeaderboardTest.class})
public class TestSuite {

}
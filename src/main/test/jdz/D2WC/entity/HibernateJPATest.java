
package jdz.D2WC.entity;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jdz.D2WC.entity.hero.HeroRepository;
import jdz.D2WC.entity.matchStats.MatchStatsRepository;
import jdz.D2WC.entity.player.PlayerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = { HibernateJPATest.TestJPAConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
public class HibernateJPATest {
	@Autowired protected HeroRepository heroRepo;
	@Autowired protected PlayerRepository playerSummaryRepo;
	@Autowired protected MatchStatsRepository matchStatsRepo;

	@Test
	public void assertRepositoriesInitialised() {
		assertThat(heroRepo, notNullValue());
		assertThat(playerSummaryRepo, notNullValue());
		assertThat(matchStatsRepo, notNullValue());
	}
	
	@Configuration
	@EntityScan("jdz.D2WC.entity")
	@EnableJpaRepositories(basePackages = {"jdz.D2WC.entity"})
	@EnableTransactionManagement
	public static class TestJPAConfig {	     
	    @Bean
	    public DataSource dataSource() {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName("org.h2.Driver");
	        dataSource.setUrl("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1");
	        dataSource.setUsername("test");
	        dataSource.setPassword("test");
	        return dataSource;
	    }
	}

}

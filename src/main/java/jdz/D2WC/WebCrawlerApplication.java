package jdz.D2WC;

import java.io.File;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;

import jdz.D2WC.tasks.LeaderboardTask;
import jdz.D2WC.tasks.RecursiveTask;

@SpringBootApplication
public class WebCrawlerApplication {
	@Autowired Environment env;

	public static void main(String[] args) {
		URL mySource = WebCrawlerApplication.class.getProtectionDomain().getCodeSource().getLocation();
		File rootFolder = new File(mySource.getPath());
		System.setProperty("app.root", rootFolder.getAbsolutePath());

		ApplicationContext context = SpringApplication.run(WebCrawlerApplication.class, args);

		if (leaderboardMode(args))
			context.getBean(LeaderboardTask.class).fetchLeaderboardData();
		else
			context.getBean(RecursiveTask.class).fetchPlayerData(Integer.parseInt(args[1]));
	}

	private static boolean leaderboardMode(String[] args) {
		if (args.length == 0)
			return true;
		return args[0].endsWith("0");
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
		properties.setLocation(new FileSystemResource("application.yml"));
		properties.setIgnoreResourceNotFound(false);
		return properties;
	}
}
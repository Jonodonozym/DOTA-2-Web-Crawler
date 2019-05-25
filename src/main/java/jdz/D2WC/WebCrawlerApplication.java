package jdz.D2WC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;

@SpringBootApplication
public class WebCrawlerApplication {
	@Autowired Environment env;

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(WebCrawlerApplication.class, args);
		context.getBean(WebCrawlerTask.class).runTask();
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
		properties.setLocation(new FileSystemResource("application.yml"));
		properties.setIgnoreResourceNotFound(false);
		return properties;
	}
}
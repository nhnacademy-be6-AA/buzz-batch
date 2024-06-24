package store.buzzbook.batch.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfig {

	private String driverClassName;
	private String url;
	private String username;
	private String password;
	private Integer maxIdle;
	private Integer maxTotal;
	private Integer initialSize;
	private Integer minIdle;

	@Bean
	public DataSource dataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();

		basicDataSource.setDriverClassName(driverClassName);
		basicDataSource.setUrl(url);
		basicDataSource.setUsername(username);
		basicDataSource.setPassword(password);
		basicDataSource.setMaxIdle(maxIdle);
		basicDataSource.setMaxTotal(maxTotal);
		basicDataSource.setInitialSize(initialSize);
		basicDataSource.setMinIdle(minIdle);

		basicDataSource.setValidationQuery("SELECT 1");
		basicDataSource.setTestOnReturn(false);
		basicDataSource.setTestOnBorrow(false);
		basicDataSource.setTestWhileIdle(false);

		return basicDataSource;
	}

}

package ro.championsclub.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
class DatasourceConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.hikari.schema}")
    private String schema;

    @Bean
    DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url);
        config.setDriverClassName(driver);
        config.setUsername(username);
        config.setPassword(password);
        config.setSchema(schema);

        return new HikariDataSource(config);
    }

}

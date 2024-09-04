package ro.championsclub.config;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DatasourceConfigIT {

    @Autowired
    private DataSource datasource;

    @Test
    void datasourceConfigTest() {
        assertThat(datasource).isNotNull();

        HikariDataSource hikariDataSource = (HikariDataSource) datasource;
        assertThat(hikariDataSource).isNotNull();

        assertThat(hikariDataSource.getJdbcUrl()).contains("jdbc:h2:mem:test");
        assertThat("org.h2.Driver").isEqualTo(hikariDataSource.getDriverClassName());
        assertThat("sa").isEqualTo(hikariDataSource.getUsername());
        assertThat("").isEqualTo(hikariDataSource.getPassword());
        assertThat("cc").isEqualTo(hikariDataSource.getSchema());
    }

}

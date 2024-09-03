package ro.championsclub.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@Import(AppConfigIT.TestConfig.class)
@SpringBootTest(classes = AppConfig.class)
public class AppConfigIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AsyncService asyncService;

    @Test
    void contextLoadTest() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void asyncTest() throws Exception {
        CompletableFuture<String> future = asyncService.asyncMethod();

        assertThat(future).isNotNull();
        assertThat(future.get()).isEqualTo("Hello, Async!");
    }

    // config bean for injection
    @Configuration
    static class TestConfig {

        @Bean
        public AsyncService asyncService() {
            return new AsyncService();
        }

    }

    // set up a dummy service
    @Service
    static class AsyncService {

        @Async
        public CompletableFuture<String> asyncMethod() {
            return CompletableFuture.completedFuture("Hello, Async!");
        }

    }

}

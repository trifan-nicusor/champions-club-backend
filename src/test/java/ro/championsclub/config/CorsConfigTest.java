package ro.championsclub.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CorsConfigTest {

    @Mock
    private CorsRegistry corsRegistry;

    @Mock
    private CorsRegistration corsRegistration;

    @InjectMocks
    private CorsConfig corsConfig;

    @BeforeEach
    public void setup() {
        when(corsRegistry.addMapping(anyString())).thenReturn(corsRegistration);

        when(corsRegistration.allowedOrigins(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders(any(String[].class))).thenReturn(corsRegistration);
        when(corsRegistration.allowCredentials(anyBoolean())).thenReturn(corsRegistration);
        when(corsRegistration.maxAge(anyLong())).thenReturn(corsRegistration);

        setPrivateField(corsConfig, "allowedOrigins", new String[]{"http://localhost:3000"});
        setPrivateField(corsConfig, "allowedHeaders", new String[]{"Content-Type", "Authorization"});
        setPrivateField(corsConfig, "allowedMethods", new String[]{"GET", "POST", "PUT", "DELETE"});
        setPrivateField(corsConfig, "allowCredentials", true);
        setPrivateField(corsConfig, "maxAge", 3600L);
    }

    @Test
    public void addCorsMappingsTest() {
        corsConfig.addCorsMappings(corsRegistry);

        verify(corsRegistry).addMapping("/**");
        verify(corsRegistration).allowedOrigins("http://localhost:3000");
        verify(corsRegistration).allowedMethods("GET", "POST", "PUT", "DELETE");
        verify(corsRegistration).allowedHeaders("Content-Type", "Authorization");
        verify(corsRegistration).allowCredentials(true);
        verify(corsRegistration).maxAge(3600L);
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);

            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Failed to set values through reflection due to {}", e.getMessage());
        }
    }

}

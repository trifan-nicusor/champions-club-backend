package ro.championsclub.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ro.championsclub.entity.User;
import ro.championsclub.entity.UuidToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UuidTokenRepositoryTest {

    @Autowired
    private UuidTokenRepository uuidTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private UuidToken validUuidToken;
    private UuidToken invavalidUuidToken;

    @BeforeEach
    void setup() {
        user = User.builder()
                .firstName("First")
                .lastName("Last")
                .email("email@email.com")
                .password("password")
                .isEnabled(true)
                .confirmedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        validUuidToken = UuidToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        invavalidUuidToken = UuidToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().minusDays(1))
                .user(user)
                .build();

        uuidTokenRepository.saveAll(List.of(validUuidToken, invavalidUuidToken));
    }

    @Test
    void findByTokenTest() {
        Optional<UuidToken> uuidToken = uuidTokenRepository.findByToken(validUuidToken.getToken());

        assertThat(uuidToken).isPresent();
    }

    @Test
    void findAllByUserTest() {
        List<UuidToken> tokens = uuidTokenRepository.findAllByUser(user);

        assertThat(tokens.size()).isEqualTo(1);
    }

    @Test
    void getByTokenTest() {
        Throwable thrown = catchThrowable(() -> uuidTokenRepository.getByToken(invavalidUuidToken.getToken()));

        assertThat(thrown)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No valid token found");
    }

}

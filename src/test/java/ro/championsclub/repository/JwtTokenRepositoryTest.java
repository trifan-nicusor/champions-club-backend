package ro.championsclub.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ro.championsclub.entity.JwtToken;
import ro.championsclub.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JwtTokenRepositoryTest {

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private JwtToken token1;
    private JwtToken token2;

    @BeforeEach
    void setup() {
        user = User.builder()
                .email("test@email.com")
                .password("test")
                .firstName("First")
                .lastName("Last")
                .isEnabled(true)
                .confirmedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        token1 = JwtToken.builder()
                .token("random")
                .user(user)
                .build();

        token2 = JwtToken.builder()
                .token("random 2")
                .user(user)
                .build();

        jwtTokenRepository.saveAll(List.of(token1, token2));
    }

    @Test
    void findAllByUser() {
        List<JwtToken> tokens = jwtTokenRepository.findAll();

        assertThat(tokens)
                .extracting(JwtToken::getToken)
                .containsExactlyInAnyOrder(token1.getToken(), token2.getToken());

        jwtTokenRepository.deleteAllByUser(user);

        tokens = jwtTokenRepository.findAll();

        assertThat(tokens)
                .extracting(JwtToken::getToken)
                .doesNotContain(token1.getToken(), token2.getToken());
    }

}

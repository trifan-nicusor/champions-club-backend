package ro.championsclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ro.championsclub.entity.JwtToken;
import ro.championsclub.entity.User;
import ro.championsclub.repository.JwtTokenRepository;
import ro.championsclub.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class JwtTokenServiceIT {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        jwtTokenRepository.deleteAll();
        userRepository.deleteAll();

        user = User.builder()
                .email("test@email.com")
                .password("test")
                .firstName("First")
                .lastName("Last")
                .isEnabled(true)
                .build();

        userRepository.save(user);
    }

    @Test
    void saveJwtTokenTest() {
        var token = "random-token";

        jwtTokenService.saveJwtToken(user, token);

        List<JwtToken> tokens = jwtTokenRepository.findAll().stream()
                .filter(tkn -> tkn.getToken().equals(token))
                .toList();

        assertThat(tokens).isNotEmpty();

        var jwtToken = tokens.getFirst();

        assertThat(jwtToken.getToken()).isEqualTo(token);
        assertThat(jwtToken.getUser().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void revokeAllUserJwtTokensTest() {
        var token = "random-token";
        var token1 = "random-token-1";

        jwtTokenService.saveJwtToken(user, token);
        jwtTokenService.saveJwtToken(user, token1);

        List<JwtToken> tokens = jwtTokenRepository.findAll().stream()
                .filter(tkn -> tkn.getUser().getEmail().equals(user.getEmail()))
                .toList();

        assertThat(tokens).isNotEmpty();

        jwtTokenRepository.deleteAllByUser(user);

        tokens = jwtTokenRepository.findAll().stream()
                .filter(tkn -> tkn.getUser().getEmail().equals(user.getEmail()))
                .toList();

        assertThat(tokens).isEmpty();
    }

}

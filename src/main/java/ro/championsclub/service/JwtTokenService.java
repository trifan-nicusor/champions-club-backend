package ro.championsclub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.championsclub.entity.JwtToken;
import ro.championsclub.entity.User;
import ro.championsclub.repository.JwtTokenRepository;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenRepository jwtTokenRepository;

    public void saveJwtToken(User user, String jwtToken) {
        var token = JwtToken.builder()
                .user(user)
                .token(jwtToken)
                .build();

        jwtTokenRepository.save(token);
    }

    public void revokeAllUserJwtTokens(User user) {
        jwtTokenRepository.deleteAllByUser(user);
    }

}

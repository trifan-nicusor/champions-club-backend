package ro.championsclub.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.championsclub.component.EmailBuilder;
import ro.championsclub.dto.request.EmailRequest;
import ro.championsclub.dto.request.LoginRequest;
import ro.championsclub.dto.request.RegisterRequest;
import ro.championsclub.dto.request.ResetPasswordRequest;
import ro.championsclub.dto.response.LoginResponse;
import ro.championsclub.entity.User;
import ro.championsclub.entity.UuidToken;
import ro.championsclub.exception.BusinessException;
import ro.championsclub.exception.ResourceConflictException;
import ro.championsclub.exception.TechnicalException;
import ro.championsclub.repository.UserRepository;
import ro.championsclub.repository.UuidTokenRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UuidTokenRepository uuidTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;
    private final EmailBuilder emailBuilder;
    private final AuthenticationManager authenticationManager;

    @Value("${host.port}")
    private String hostAndPort;

    @Value("${uuid.token.expiration}")
    private int expireTime;

    public void register(RegisterRequest request) {
        String email = request.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("Email already taken");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        jwtTokenService.saveJwtToken(user, jwtToken);
        sendConfirmationEmail(user);
    }

    @Transactional
    public void confirmAccount(String token) {
        var uuidToken = uuidTokenRepository.getByToken(token);

        uuidToken.setExpiresAt(LocalDateTime.now());

        userRepository.enableUser(uuidToken.getUser().getEmail());
    }

    public void resendConfirmationEmail(EmailRequest request) {
        var user = userRepository.getUnconfirmedUser(request.getEmail());

        List<UuidToken> tokens = uuidTokenRepository.findAllByUser(user);

        if (!tokens.isEmpty()) {
            throw new BusinessException("Email already sent");
        }

        sendConfirmationEmail(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user;
        var email = request.getEmail();

        try {
            user = userRepository.getByEmail(email);

            if (!user.isEnabled()) {
                throw new BusinessException("Account must be enabled before login");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (EntityNotFoundException | AuthenticationException e) {
            throw new BusinessException("Invalid email or password");
        }

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        jwtTokenService.revokeAllUserJwtTokens(user);
        jwtTokenService.saveJwtToken(user, accessToken);

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new TechnicalException("Failed to get authorization header");
        }

        var refreshToken = authHeader.substring(7);
        var email = jwtService.extractUsername(refreshToken);

        if (email != null) {
            var user = userRepository.getByEmail(email);

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);

                jwtTokenService.revokeAllUserJwtTokens(user);
                jwtTokenService.saveJwtToken(user, accessToken);

                return new LoginResponse(accessToken, refreshToken);
            }
        }

        throw new TechnicalException("Failed to extract user details from header");
    }

    public void sendPasswordResetEmail(EmailRequest request) {
        var email = request.getEmail();
        var user = userRepository.getByEmail(email);
        var token = UUID.randomUUID().toString();
        var link = "http://localhost:4400/reset-password?resetToken=" + token;

        var resetToken = UuidToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusMinutes(expireTime))
                .user(user)
                .build();

        uuidTokenRepository.save(resetToken);

        var name = user.getFirstName();
        var builtEmail = emailBuilder.forgotPasswordEmail(name, link);

        emailService.send(email, builtEmail);
    }

    @Transactional
    public void resetPassword(String token, ResetPasswordRequest request) {
        var uuidToken = uuidTokenRepository.getByToken(token);

        uuidTokenRepository.delete(uuidToken);

        var email = uuidToken.getUser().getEmail();
        var password = passwordEncoder.encode(request.getPassword());

        userRepository.changePassword(email, password);
    }

    private void sendConfirmationEmail(User user) {
        var token = UUID.randomUUID().toString();
        var link = hostAndPort + "/api/v1/auth/confirm-account?token=" + token;

        var uuidToken = UuidToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusMinutes(expireTime))
                .user(user)
                .build();

        uuidTokenRepository.save(uuidToken);

        var email = emailBuilder.confirmationEmail(user.getFirstName(), link);
        emailService.send(user.getEmail(), email);
    }

}

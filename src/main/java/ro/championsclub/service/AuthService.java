package ro.championsclub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.championsclub.component.EmailBuilder;
import ro.championsclub.dto.request.RegisterRequest;
import ro.championsclub.entity.User;
import ro.championsclub.entity.UuidToken;
import ro.championsclub.exception.ResourceConflictException;
import ro.championsclub.repository.UserRepository;
import ro.championsclub.repository.UuidTokenRepository;

import java.time.LocalDateTime;
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
                .firstName("3333333333333333333333333333333333333333333333333333333333333333333")
                .lastName(request.getLastName())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        jwtTokenService.saveJwtToken(user, jwtToken);
        sendConfirmationEmail(user);
    }

    private void sendConfirmationEmail(User user) {
        var token = UUID.randomUUID().toString();
        var link = hostAndPort + "/api/v1/auth/confirm-account?confirmationToken=" + token;

        var uuidToken = UuidToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusMinutes(expireTime))
                .user(user)
                .build();

        uuidTokenRepository.save(uuidToken);

        String email = emailBuilder.confirmationEmail(user.getFirstName(), link);
        emailService.send(user.getEmail(), email);
    }

    /*private final JwtTokenRepository jwtTokenRepository;
    private final UuidTokenRepository uuidTokenRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final EmailBuilder emailBuilder;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) {
        String email = request.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("An account with this email already exists");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        saveJwtToken(user, jwtToken);
        sendConfirmationEmail(user);
    }

    @Transactional
    public void confirmAccount(String token) {
        var uuidToken = uuidTokenRepository.getByToken(token);

        uuidToken.setExpiresAt(LocalDateTime.now());

        userRepository.enableUser(uuidToken.getUser().getEmail());
    }

    public void resendConfirmationEmail(EmailRequest request) {
        var user = userRepository.getByEmail(request.getEmail());

        if (user.getConfirmedAt() != null) {
            throw new BusinessException("User already confirmed");
        }

        List<UuidToken> tokens = uuidTokenRepository.findAllByUser(user);

        if (!tokens.isEmpty()) {
            throw new BusinessException("Email already sent");
        }

        sendConfirmationEmail(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user;

        try {
            user = userRepository.getByEmail(request.getEmail());

            if (!user.isEnabled()) {
                var emailRequest = new EmailRequest(user.getEmail());

                resendConfirmationEmail(emailRequest);

                throw new BusinessException("Account must be confirmed before login, " +
                        "new confirmation sent");
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

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserJwtTokens(user);
        saveJwtToken(user, token);

        return new LoginResponse(token, refreshToken);
    }

    public LoginResponse refreshToken(HttpServletRequest request) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new TechnicalException("Failed to get authorization header");
        }

        refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = userRepository.getByEmail(userEmail);

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);

                revokeAllUserJwtTokens(user);
                saveJwtToken(user, accessToken);

                return new LoginResponse(accessToken, refreshToken);
            }
        }

        throw new TechnicalException("Failed to extract user details from header");
    }

    public void sendPasswordResetEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) return;

        final String token = UUID.randomUUID().toString();
        final String link = "http://localhost:4400/reset-password?resetToken=" + token;

        var resetToken = UuidToken.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusMinutes(expireTime))
                .user(user.get())
                .build();

        uuidTokenRepository.save(resetToken);

        String name = user.get().getFirstName();

        emailService.send(email, emailBuilder.forgotPasswordEmail(name, link));
    }

    @Transactional
    public void resetPassword(String token, ResetPasswordRequest request) {
        UuidToken uuidToken = uuidTokenRepository.getByToken(token);

        uuidTokenRepository.delete(uuidToken);

        String email = uuidToken.getUser().getEmail();
        String password = passwordEncoder.encode(request.getPassword());

        userRepository.changePassword(email, password);
    }
    }*/
}
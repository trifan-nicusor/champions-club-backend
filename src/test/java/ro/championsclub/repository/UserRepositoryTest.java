package ro.championsclub.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ro.championsclub.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User validUser;
    private User disabledUser;

    @BeforeEach
    void setup() {
        validUser = User.builder()
                .email("test@email.com")
                .password("test")
                .firstName("First")
                .lastName("Last")
                .isEnabled(true)
                .build();

        var invalidUser = User.builder()
                .email("test1@email.com")
                .password("test1")
                .firstName("First")
                .lastName("Last")
                .isEnabled(false)
                .isLocked(true)
                .build();

        disabledUser = User.builder()
                .email("test2@email.com")
                .password("test")
                .firstName("First")
                .lastName("Last")
                .isEnabled(false)
                .confirmedAt(null)
                .build();

        userRepository.saveAll(List.of(validUser, invalidUser, disabledUser));
    }

    @Test
    void existsByEmailTest() {
        boolean exists = userRepository.existsByEmail(validUser.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    void findByEmailAndIsEnabledTrueAndIsLockedFalse() {
        var email = validUser.getEmail();
        Optional<User> optionalUser = userRepository.findByEmailAndIsEnabledTrueAndIsLockedFalse(email);

        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    void enableUserTest() {
        var email = disabledUser.getEmail();
        Optional<User> optionalUser = userRepository.findUnconfirmedUser(email);

        assertThat(optionalUser).isPresent();

        var user = optionalUser.get();

        assertThat(user.isEnabled()).isFalse();
        assertThat(user.getConfirmedAt()).isNull();

        LocalDateTime updatedAt = user.getUpdatedAt();

        userRepository.enableUser(email);

        entityManager.flush();
        entityManager.clear();

        // after calling enableUser()
        optionalUser = userRepository.findByEmailAndIsEnabledTrueAndIsLockedFalse(email);

        assertThat(optionalUser).isPresent();

        user = optionalUser.get();

        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getConfirmedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isAfter(updatedAt);
    }

    @Test
    void changePasswordTest() {
        var email = validUser.getEmail();
        Optional<User> optionalUser = userRepository.findByEmailAndIsEnabledTrueAndIsLockedFalse(email);

        assertThat(optionalUser).isPresent();

        var user = optionalUser.get();

        assertThat(user.getPassword()).isEqualTo("test");

        LocalDateTime updatedAt = user.getUpdatedAt();

        String newPassword = "newPassword";

        userRepository.changePassword(email, newPassword);

        entityManager.flush();
        entityManager.clear();

        // after calling changePassword()
        optionalUser = userRepository.findByEmailAndIsEnabledTrueAndIsLockedFalse(email);

        assertThat(optionalUser).isPresent();

        user = optionalUser.get();

        assertThat(user.getPassword()).isEqualTo(newPassword);
        assertThat(user.getUpdatedAt()).isAfter(updatedAt);
    }

    @Test
    void getByEmailTest() {
        Throwable thrown = catchThrowable(() -> userRepository.getByEmail("random"));

        assertThat(thrown).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No user with this email found");
    }

    @Test
    void getUnconfirmedUser() {
        Throwable thrown = catchThrowable(() -> userRepository.findUnconfirmedUser("random"));

        assertThat(thrown).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No user with this email found");
    }

}

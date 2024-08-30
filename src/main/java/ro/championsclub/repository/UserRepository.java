package ro.championsclub.repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ro.championsclub.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndIsEnabledTrueAndIsLockedFalse(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User " +
            "SET isEnabled = true, confirmedAt = CURRENT_TIMESTAMP, updatedAt = CURRENT_TIMESTAMP " +
            "WHERE email = :email")
    void enableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User " +
            "SET password = :password, updatedAt = CURRENT_TIMESTAMP " +
            "WHERE email = :email")
    void changePassword(String email, String password);

    default User getByEmail(String email) {
        return findByEmailAndIsEnabledTrueAndIsLockedFalse(email)
                .orElseThrow(() -> new EntityNotFoundException("No user with this email found"));
    }

}

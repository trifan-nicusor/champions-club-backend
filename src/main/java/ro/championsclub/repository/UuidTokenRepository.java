package ro.championsclub.repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.championsclub.entity.User;
import ro.championsclub.entity.UuidToken;

import java.util.List;
import java.util.Optional;

public interface UuidTokenRepository extends JpaRepository<UuidToken, Long> {

    Optional<UuidToken> findByToken(String token);

    List<UuidToken> findAllByUser(User user);

    default UuidToken getByToken(String token) {
        return findByToken(token).orElseThrow(
                () -> new EntityNotFoundException("No valid token found"));
    }

}

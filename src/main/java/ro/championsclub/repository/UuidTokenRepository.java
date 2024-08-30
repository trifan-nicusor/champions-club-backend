package ro.championsclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.championsclub.entity.UuidToken;

public interface UuidTokenRepository extends JpaRepository<UuidToken, Long> {

    /*List<UuidToken> findAllByUser(User user);

    Optional<UuidToken> findByToken(String token);

    default UuidToken getByToken(String token) {
        return findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("No valid token found"));
    }*/

}
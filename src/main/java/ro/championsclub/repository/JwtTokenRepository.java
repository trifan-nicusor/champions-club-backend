package ro.championsclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ro.championsclub.entity.JwtToken;
import ro.championsclub.entity.User;

import java.util.List;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE JwtToken " +
            "SET isRevoked = true, isExpired = true " +
            "WHERE user = :user")
    void deleteAllByUser(User user);

    List<JwtToken> findAllByUser(User user);

}
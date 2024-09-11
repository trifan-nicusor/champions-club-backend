package ro.championsclub.repository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ro.championsclub.entity.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    boolean existsByName(String name);

    Optional<Subscription> findByNameAndIsActiveTrue(String name);

    List<Subscription> findAllByIsActiveTrue();

    List<Subscription> findAllByIsActiveFalse();

    @Modifying
    @Transactional
    @Query("UPDATE Subscription " +
            "SET isActive = false " +
            "WHERE name = :name")
    void disable(String name);

    default Subscription getByName(String name) {
        return findByNameAndIsActiveTrue(name).orElseThrow(
                () -> new EntityNotFoundException("Subscription with name: " + name + " not found"));
    }

}

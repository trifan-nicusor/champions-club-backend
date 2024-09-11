package ro.championsclub.repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ro.championsclub.entity.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {

    boolean existsByName(String name);

    boolean existsByCode(String name);

    Optional<Discount> findByNameAndIsActiveTrue(String name);
    
    List<Discount> findAllByIsActiveTrue();

    List<Discount> findAllByIsActiveFalse();

    @Modifying
    @Transactional
    @Query("UPDATE Discount " +
            "SET isActive = false " +
            "WHERE name = :name")
    void disable(String name);

    default Discount getByName(String name) {
        return findByNameAndIsActiveTrue(name).orElseThrow(
                () -> new EntityNotFoundException("Discount with name: " + name + " not found")
        );
    }

}

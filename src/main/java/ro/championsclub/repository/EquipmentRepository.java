package ro.championsclub.repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ro.championsclub.entity.Equipment;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    boolean existsByName(String name);

    Optional<Equipment> findByNameAndIsActiveTrue(String name);

    List<Equipment> findAllByIsActiveTrue();

    List<Equipment> findAllByIsActiveFalse();

    @Modifying
    @Transactional
    @Query("UPDATE Equipment " +
            "SET isActive = false " +
            "WHERE name = :name")
    void disable(String name);

    default Equipment getByName(String name) {
        return findByNameAndIsActiveTrue(name).orElseThrow(
                () -> new EntityNotFoundException("Equipment with name: " + name + " not found"));
    }

}

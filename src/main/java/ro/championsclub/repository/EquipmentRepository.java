package ro.championsclub.repository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.championsclub.entity.Equipment;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    boolean existsByName(String name);

    Optional<Equipment> findByIdAndIsActiveTrue(int id);

    List<Equipment> findAllByIsActiveTrue();

    List<Equipment> findAllByIsActiveFalse();

    default Equipment getById(int id) {
        return findByIdAndIsActiveTrue(id).orElseThrow(
                () -> new EntityNotFoundException("Equipment with id: " + id + " not found"));
    }

}

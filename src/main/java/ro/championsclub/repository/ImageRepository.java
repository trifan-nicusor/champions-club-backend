package ro.championsclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.championsclub.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    boolean existsByName(String name);

}

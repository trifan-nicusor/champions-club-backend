package ro.championsclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.championsclub.entity.Cart;
import ro.championsclub.entity.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUserAndIsOrderedFalse(User user);

    default Optional<Cart> findByUser(User user) {
        return findByUserAndIsOrderedFalse(user);
    }

}

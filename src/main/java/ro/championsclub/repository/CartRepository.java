package ro.championsclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ro.championsclub.entity.Cart;
import ro.championsclub.entity.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUserAndIsOrderedFalse(User user);

    @Modifying
    @Transactional
    @Query("UPDATE Cart " +
            "SET isOrdered = true " +
            "WHERE id = :cartId")
    void setCartOrdered(long cartId);

    default Optional<Cart> findByUser(User user) {
        return findByUserAndIsOrderedFalse(user);
    }

}

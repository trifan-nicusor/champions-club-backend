package ro.championsclub.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ro.championsclub.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Product " +
            "WHERE cart.id = :cartId AND subscription.id = :subId")
    void removeProduct(long cartId, long subId);

}

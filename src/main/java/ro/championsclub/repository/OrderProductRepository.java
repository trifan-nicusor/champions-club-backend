package ro.championsclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.championsclub.entity.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}

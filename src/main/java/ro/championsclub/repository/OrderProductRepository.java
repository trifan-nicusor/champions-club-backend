package ro.championsclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.championsclub.entity.OrderSubscriptions;

public interface OrderProductRepository extends JpaRepository<OrderSubscriptions, Long> {

}

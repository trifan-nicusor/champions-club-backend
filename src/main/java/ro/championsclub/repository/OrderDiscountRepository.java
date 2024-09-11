package ro.championsclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.championsclub.entity.OrderDiscount;

public interface OrderDiscountRepository extends JpaRepository<OrderDiscount, Long> {

}
package ro.championsclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.championsclub.entity.Order;
import ro.championsclub.entity.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);

}

package ro.championsclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.championsclub.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

}

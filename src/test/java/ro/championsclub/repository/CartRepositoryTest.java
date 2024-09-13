package ro.championsclub.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ro.championsclub.entity.Cart;
import ro.championsclub.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Cart cart;

    @BeforeEach
    void setup() {
        user = User.builder()
                .email("test@email.com")
                .password("test")
                .firstName("First")
                .lastName("Last")
                .isEnabled(true)
                .confirmedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        cart = Cart.builder()
                .user(user)
                .build();

        cartRepository.save(cart);
    }

    @Test
    public void findByUserAndIsOrderedFalseTest() {
        Optional<Cart> optionalCart = cartRepository.findByUserAndIsOrderedFalse(user);

        assertThat(optionalCart).isPresent();
        assertThat(optionalCart.get().getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(optionalCart.get().getIsOrdered()).isFalse();
    }

    // includes test for 'findByUser()' method
    @Test
    public void setCartOrderedTest() {
        long cartId = cart.getId();

        assertThat(cart.getIsOrdered()).isFalse();

        cartRepository.setCartOrdered(cartId);

        Optional<Cart> updatedCart = cartRepository.findByUser(user);

        assertThat(updatedCart).isNotPresent();
    }

}

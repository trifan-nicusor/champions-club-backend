package ro.championsclub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class CartView {

    private BigDecimal total;
    private BigDecimal discount;
    private Set<ProductView> products;
    private Set<DiscountView> discounts;

}
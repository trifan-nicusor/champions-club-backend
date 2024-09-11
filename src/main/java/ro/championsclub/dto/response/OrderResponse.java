package ro.championsclub.dto.response;

import java.util.Set;

public record OrderResponse(Long id,
                            String status,
                            String paymentMethod,
                            Set<OrderProductResponse> products,
                            Set<OrderDiscountResponse> discount) {

}

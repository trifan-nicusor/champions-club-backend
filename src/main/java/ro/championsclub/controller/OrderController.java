package ro.championsclub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.championsclub.dto.response.OrderResponse;
import ro.championsclub.entity.User;
import ro.championsclub.service.OrderService;

import java.util.List;

@Tag(name = "Orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService service;

    @Operation(summary = "[only for users] get my orders")
    @GetMapping
    public List<OrderResponse> getMyOrders(@AuthenticationPrincipal User user) {
        return service.getMyOrders(user);
    }

}


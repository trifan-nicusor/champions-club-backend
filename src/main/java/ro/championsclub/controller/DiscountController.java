package ro.championsclub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.championsclub.dto.ErrorDto;
import ro.championsclub.dto.request.DiscountRequest;
import ro.championsclub.dto.request.DiscountUpdateRequest;
import ro.championsclub.dto.response.DiscountAdminView;
import ro.championsclub.service.DiscountService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discount")
@Validated
@RequiredArgsConstructor
@Tag(name = "Discount Controller")
public class DiscountController {

    private final DiscountService service;

    @Operation(summary = "[only for admins] save discount")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Another discount already saved with this name or code",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void saveDiscount(@Valid @RequestBody DiscountRequest request) {
        service.saveDiscount(request);
    }

    @Operation(summary = "[only for admins] delete discount")
    @ApiResponse(responseCode = "204")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteDiscount(@PathVariable String name) {
        service.deleteDiscount(name);
    }

    @Operation(summary = "[only for admins] update discount")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Discount with this name or code already exists",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @PatchMapping("/update/{name}")
    public void updateDiscount(
            @PathVariable String name,
            @Valid @RequestBody DiscountUpdateRequest request
    ) {
        service.updateDiscount(name, request);
    }

    @Operation(summary = "[only for admins] get all active discounts")
    @GetMapping("/active-discounts")
    public List<DiscountAdminView> getAllActiveDiscounts() {
        return service.getAllActiveDiscounts();
    }

    @Operation(summary = "[only for admins] get all inactive subscriptions")
    @GetMapping("/inactive-discounts")
    public List<DiscountAdminView> getAllInactiveDiscounts() {
        return service.getAllInactiveDiscounts();
    }

}

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
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.championsclub.dto.ErrorDto;
import ro.championsclub.dto.request.SubscriptionRequest;
import ro.championsclub.dto.request.SubscriptionUpdateRequest;
import ro.championsclub.dto.response.SubscriptionAdminView;
import ro.championsclub.dto.response.SubscriptionView;
import ro.championsclub.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscription")
@Validated
@RequiredArgsConstructor
@Tag(name = "Subscription Controller")
public class SubscriptionController {

    private final SubscriptionService service;

    @Operation(summary = "[only for admins] save subscription")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid image format/failed to get image bytes",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Another equipment/image already saved with this name",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping({
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public void saveSubscription(
            @RequestPart MultipartFile image,
            @Valid @RequestPart SubscriptionRequest request
    ) {
        service.saveSubscription(image, request);
    }

    @Operation(summary = "[only for admins] delete subscription")
    @ApiResponse(responseCode = "204")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteSubscription(@PathVariable String name) {
        service.deleteSubscription(name);
    }

    @Operation(summary = "[only for admins] update subscription")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid image format/failed to get image bytes",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Equipment/image name name already exists",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @PatchMapping(
            path = "/update/{name}",
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public void updateSubscription(
            @PathVariable String name,
            @Nullable @RequestPart MultipartFile image,
            @Valid @RequestPart SubscriptionUpdateRequest request
    ) {
        service.updateSubscription(name, image, request);
    }

    @Operation(summary = "get all active subscriptions")
    @GetMapping
    public List<SubscriptionView> getAllEquipments() {
        return service.getAllSubscriptions();
    }

    @Operation(summary = "[only for admins] get all active subscriptions")
    @GetMapping("/active-subscriptions")
    public List<SubscriptionAdminView> getAllActiveEquipments() {
        return service.getAllActiveSubscriptions();
    }

    @Operation(summary = "[only for admins] get all inactive subscriptions")
    @GetMapping("/inactive-subscriptions")
    public List<SubscriptionAdminView> getAllInactiveEquipments() {
        return service.getAllInactiveSubscriptions();
    }

}

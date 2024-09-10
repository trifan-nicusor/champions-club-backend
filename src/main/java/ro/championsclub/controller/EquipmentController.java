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
import ro.championsclub.dto.request.EquipmentRequest;
import ro.championsclub.dto.request.EquipmentUpdateRequest;
import ro.championsclub.dto.response.EquipmentAdminView;
import ro.championsclub.dto.response.EquipmentView;
import ro.championsclub.service.EquipmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipment")
@Validated
@RequiredArgsConstructor
@Tag(name = "Equipment Controller")
public class EquipmentController {

    private final EquipmentService service;

    @Operation(summary = "[only for admins] save equipment")
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
    @PostMapping(
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public void saveEquipment(
            @RequestPart MultipartFile image,
            @Valid @RequestPart EquipmentRequest request
    ) {
        service.saveEquipment(request, image);
    }

    @Operation(summary = "[only for admins] disable equipment")
    @ApiResponse(responseCode = "204")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void disableEquipment(@PathVariable String name) {
        service.disableEquipment(name);
    }

    @Operation(summary = "[only for admins] update equipment")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid image format/failed to get image bytes",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Equipment/image name already exists",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @PatchMapping(
            path = "/{name}",
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public EquipmentAdminView updateEquipment(
            @PathVariable String name,
            @Nullable @RequestPart MultipartFile image,
            @Valid @RequestPart EquipmentUpdateRequest request
    ) {
        return service.updateEquipment(name, image, request);
    }

    @Operation(summary = "[only for admins] get all active equipments")
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = EquipmentAdminView.class))
    )
    @GetMapping("/admin/active")
    public List<EquipmentAdminView> getAllActiveEquipments() {
        return service.getAllActiveEquipments();
    }

    @Operation(summary = "[only for admins] get all inactive equipments")
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = EquipmentAdminView.class))
    )
    @GetMapping("/admin/inactive")
    public List<EquipmentAdminView> getAllInactiveEquipments() {
        return service.getAllInactiveEquipments();
    }

    @Operation(summary = "get all active equipments")
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = EquipmentView.class))
    )
    @GetMapping
    public List<EquipmentView> getAllEquipments() {
        return service.getAllEquipments();
    }

}

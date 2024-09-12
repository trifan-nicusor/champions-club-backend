package ro.championsclub.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.championsclub.dto.ErrorDto;
import ro.championsclub.dto.ValidationDto;
import ro.championsclub.dto.request.EmailRequest;
import ro.championsclub.dto.request.LoginRequest;
import ro.championsclub.dto.request.RegisterRequest;
import ro.championsclub.dto.request.ResetPasswordRequest;
import ro.championsclub.dto.response.LoginResponse;
import ro.championsclub.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Auth Controller")
public class AuthController {

    private final AuthService service;

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully registered"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "An account with this email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Unable to process the request due to invalid fields",
                    content = @Content(schema = @Schema(implementation = ValidationDto.class))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest request) {
        service.register(request);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No valid token found",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @GetMapping("/confirm-account")
    public void confirmAccount(@RequestParam String token) {
        service.confirmAccount(token);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully resent"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Account already confirmed",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @PostMapping("/resend-confirmation-email")
    public void resendConfirmationEmail(@Valid @RequestBody EmailRequest request) {
        service.resendConfirmationEmail(request);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Account not confirmed or invalid email/password",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User could not be extracted from JWT",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @PostMapping("/refresh-token")
    public LoginResponse refreshToken(HttpServletRequest request) {
        return service.refreshToken(request);
    }
    
    @PostMapping("/forgot-password")
    public void forgotPassword(@Valid @RequestBody EmailRequest request) {
        service.sendPasswordResetEmail(request);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No valid token found",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))
            )
    })
    @PatchMapping("/reset-password")
    public void resetPassword(
            @RequestParam String token,
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        service.resetPassword(token, request);
    }

}

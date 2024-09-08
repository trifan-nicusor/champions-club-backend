package ro.championsclub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ro.championsclub.component.ConstraintValidator;
import ro.championsclub.dto.request.EmailRequest;
import ro.championsclub.dto.request.LoginRequest;
import ro.championsclub.dto.request.RegisterRequest;
import ro.championsclub.dto.response.LoginResponse;
import ro.championsclub.repository.UserRepository;
import ro.championsclub.service.AuthService;
import ro.championsclub.service.JwtService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ConstraintValidator constraintValidator;

    private static final String PATH = "/api/v1/auth";

    @Test
    void registerTest() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("First");
        request.setLastName("Last");
        request.setEmail("test@example.com");
        request.setPassword("test");

        doNothing().when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post(PATH + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated());
    }

    @Test
    void confirmAccountTest() throws Exception {
        String token = UUID.randomUUID().toString();

        doNothing().when(authService).confirmAccount(anyString());

        mockMvc.perform(get(PATH + "/confirm-account")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void resendConfirmationEmailTest() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setEmail("test@example.com");

        doNothing().when(authService).resendConfirmationEmail(any(EmailRequest.class));

        mockMvc.perform(post(PATH + "/resend-confirmation-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    void loginTest() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        LoginResponse expectedResponse = new LoginResponse("accessToken", "refreshToken");

        when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post(PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

}

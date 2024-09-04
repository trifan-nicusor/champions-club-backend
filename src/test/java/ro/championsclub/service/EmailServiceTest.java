package ro.championsclub.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import ro.championsclub.exception.TechnicalException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private MimeMessage mimeMessage;
    private final String to = "test@example.com";
    private final String email = "<h1>Test Email</h1>";

    @BeforeEach
    public void setup() {
        mimeMessage = Mockito.mock(MimeMessage.class);
        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    public void sendEmailSuccessTest() throws Exception {
        emailService.send(to, email);

        verify(mailSender).send(mimeMessage);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom("no-reply@championsclub.com");
        helper.setTo(to);
        helper.setSubject("no-reply: championsclub");
        helper.setText(email, true);
    }

    @Test
    public void sendEmailFailureTest() {
        doThrow(new RuntimeException("Mail sending failed")).when(mailSender).send(any(MimeMessage.class));

        assertThrows(TechnicalException.class, () -> emailService.send(to, email));
    }
    
}

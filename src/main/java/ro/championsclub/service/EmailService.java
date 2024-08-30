package ro.championsclub.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ro.championsclub.exception.TechnicalException;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void send(String to, String email) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("no-reply@championsclub.com");
            helper.setTo(to);
            helper.setSubject("no-reply: championsclub");
            helper.setText(email, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new TechnicalException(e.getMessage());
        }
    }

}
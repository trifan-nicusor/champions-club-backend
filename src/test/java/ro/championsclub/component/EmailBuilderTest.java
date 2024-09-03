package ro.championsclub.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailBuilderTest {

    private EmailBuilder emailBuilder;

    private final String name = "John Doe";
    private final String link = "https://example.com/test";

    @BeforeEach
    void setup() {
        emailBuilder = new EmailBuilder();
    }

    @Test
    void confirmationEmailTest() {
        String email = emailBuilder.confirmationEmail(name, link);

        assertThat(email).contains("Hi " + name + ",");
        assertThat(email).contains("<a href=\"" + link + "\">Activate Now</a>");
        assertThat(email).contains("Confirm your email");
    }

    @Test
    void forgotPasswordEmailTest() {
        String email = emailBuilder.forgotPasswordEmail(name, link);

        assertThat(email).contains("Hi " + name + ",");
        assertThat(email).contains("<a href=\"" + link + "\">Reset Password</a>");
        assertThat(email).contains("Password reset");
    }

}

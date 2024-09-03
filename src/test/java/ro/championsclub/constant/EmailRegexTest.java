package ro.championsclub.constant;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class EmailRegexTest {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EmailRegex.EXPRESSION, Pattern.CASE_INSENSITIVE);

    @Test
    public void validEmailsTest() {
        assertThat(EMAIL_PATTERN.matcher("simple@example.com").matches()).isTrue();
        assertThat(EMAIL_PATTERN.matcher("very.common@example.com").matches()).isTrue();
        assertThat(EMAIL_PATTERN.matcher("other.email-with-hyphen@example.com").matches()).isTrue();
        assertThat(EMAIL_PATTERN.matcher("other.email-with-hyphen@example.com").matches()).isTrue();
        assertThat(EMAIL_PATTERN.matcher("fully-qualified-domain@example.com").matches()).isTrue();
        assertThat(EMAIL_PATTERN.matcher("user.name+tag+sorting@example.com").matches()).isTrue();
        assertThat(EMAIL_PATTERN.matcher("example@s.example").matches()).isTrue();
        assertThat(EMAIL_PATTERN.matcher("admin@mailserver.com").matches()).isTrue();
        assertThat(EMAIL_PATTERN.matcher("user-@example.org").matches()).isTrue();
    }

    @Test
    public void invalidEmailsTest() {
        assertThat(EMAIL_PATTERN.matcher("user-@example-org").matches()).isFalse();
        assertThat(EMAIL_PATTERN.matcher("plainaddress").matches()).isFalse();
        assertThat(EMAIL_PATTERN.matcher("@missingusername.com").matches()).isFalse();
        assertThat(EMAIL_PATTERN.matcher("username@.com").matches()).isFalse();
        assertThat(EMAIL_PATTERN.matcher("username@yahoo.com.").matches()).isFalse();
        assertThat(EMAIL_PATTERN.matcher("username@yahoo..com").matches()).isFalse();
        assertThat(EMAIL_PATTERN.matcher(".username@yahoo.com").matches()).isFalse();
        assertThat(EMAIL_PATTERN.matcher("username@yahoo1corporate").matches()).isFalse();
        assertThat(EMAIL_PATTERN.matcher("username@.domain.com").matches()).isFalse();
        assertThat(EMAIL_PATTERN.matcher("username@domain..com").matches()).isFalse();
    }

}

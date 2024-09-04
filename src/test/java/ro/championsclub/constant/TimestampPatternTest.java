package ro.championsclub.constant;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimestampPatternTest {

    private final LocalDateTime dateTime = LocalDateTime.of(2024, 9, 3, 14, 30, 45);
    private final String actual = "2024-09-03 14:30:45";

    @Test
    public void dateTimeFormattingTest() {
        String formattedDate = dateTime.format(TimestampPattern.FORMATTER);

        assertThat(actual).isEqualTo(formattedDate);
    }

    @Test
    public void dateTimeParsingTest() {
        LocalDateTime parsedDateTime = LocalDateTime.parse(actual, TimestampPattern.FORMATTER);

        assertThat(dateTime).isEqualTo(parsedDateTime);
    }

    @Test
    public void invalidDateTimeParsingTest() {
        String invalidDateTime = "2024/09/03 14:30:45";

        assertThatThrownBy(() -> LocalDateTime.parse(invalidDateTime, TimestampPattern.FORMATTER))
                .isInstanceOf(DateTimeParseException.class);
    }

}
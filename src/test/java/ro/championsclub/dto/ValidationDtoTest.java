package ro.championsclub.dto;

import org.junit.jupiter.api.Test;
import ro.championsclub.constant.TimestampPattern;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationDtoTest {

    @Test
    public void validationDtoWithFieldAndErrorTest() {
        var field = "email";
        var message = "must not be empty";

        var validationDto = new ValidationDto(field, message);

        assertThat(field).isEqualTo(validationDto.field());
        assertThat(message).isEqualTo(validationDto.message());
        assertThat(validationDto.timestamp()).isNotNull();

        var parsedTimestamp = LocalDateTime.parse(validationDto.timestamp(), TimestampPattern.FORMATTER);
        assertThat(parsedTimestamp).isNotNull();
    }

    @Test
    public void validationDtoWithFieldErrorAndTimestampTest() {
        var field = "email";
        var error = "invalid format";
        var timestamp = "2024-09-04T10:15:30";

        var validationDto = new ValidationDto(field, error, timestamp);

        assertThat(field).isEqualTo(validationDto.field());
        assertThat(error).isEqualTo(validationDto.message());
        assertThat(timestamp).isEqualTo(validationDto.timestamp());
    }

}

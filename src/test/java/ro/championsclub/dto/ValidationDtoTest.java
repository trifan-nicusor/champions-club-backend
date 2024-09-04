package ro.championsclub.dto;

import org.junit.jupiter.api.Test;
import ro.championsclub.constant.TimestampPattern;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationDtoTest {

    @Test
    public void validationDtoWithFieldAndErrorTest() {
        String field = "email";
        String error = "must not be empty";

        ValidationDto validationDto = new ValidationDto(field, error);

        assertThat(field).isEqualTo(validationDto.field());
        assertThat(error).isEqualTo(validationDto.error());
        assertThat(validationDto.timestamp()).isNotNull();

        LocalDateTime parsedTimestamp = LocalDateTime.parse(validationDto.timestamp(), TimestampPattern.FORMATTER);
        assertThat(parsedTimestamp).isNotNull();
    }

    @Test
    public void validationDtoWithFieldErrorAndTimestampTest() {
        String field = "email";
        String error = "invalid format";
        String timestamp = "2024-09-04T10:15:30";

        ValidationDto validationDto = new ValidationDto(field, error, timestamp);

        assertThat(field).isEqualTo(validationDto.field());
        assertThat(error).isEqualTo(validationDto.error());
        assertThat(timestamp).isEqualTo(validationDto.timestamp());
    }

}

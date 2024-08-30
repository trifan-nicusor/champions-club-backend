package ro.championsclub.dto;

import ro.championsclub.constant.TimestampPattern;

import java.time.LocalDateTime;

public record ValidationDto(String field, String error, String timestamp) {

    public ValidationDto(String field, String error) {
        this(field, error, LocalDateTime.now().format(TimestampPattern.FORMATTER));
    }

}

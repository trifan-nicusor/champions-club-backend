package ro.championsclub.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TechnicalExceptionTest {

    @Test
    void technicalExceptionMessageTest() {
        String message = "This is a business exception";

        TechnicalException exception = new TechnicalException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void technicalExceptionIsThrownTest() {
        String message = "An error occurred";

        assertThatThrownBy(() -> {
                    throw new TechnicalException(message);
                }
        ).isInstanceOf(TechnicalException.class).hasMessage(message);
    }

}

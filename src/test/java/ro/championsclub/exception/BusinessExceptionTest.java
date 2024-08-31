package ro.championsclub.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BusinessExceptionTest {

    @Test
    void businessExceptionMessageTest() {
        String message = "This is a business exception";

        BusinessException exception = new BusinessException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void businessExceptionIsThrownTest() {
        String message = "An error occurred";

        assertThatThrownBy(() -> {
                    throw new BusinessException(message);
                }
        ).isInstanceOf(BusinessException.class).hasMessage(message);
    }

}

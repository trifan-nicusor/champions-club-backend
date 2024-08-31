package ro.championsclub.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceConflictExceptionTest {

    @Test
    void resourceConflictExceptionMessageTest() {
        String message = "This is a business exception";

        ResourceConflictException exception = new ResourceConflictException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void resourceConflictExceptionIsThrownTest() {
        String message = "An error occurred";

        assertThatThrownBy(() -> {
                    throw new ResourceConflictException(message);
                }
        ).isInstanceOf(ResourceConflictException.class).hasMessage(message);
    }

}

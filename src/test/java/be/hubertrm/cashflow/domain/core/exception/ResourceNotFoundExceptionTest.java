package be.hubertrm.cashflow.domain.core.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceNotFoundExceptionTest {

    @Nested
    class constructorDesigns {

        @Test
        @DisplayName("Single arg constructor sets message directly")
        void testSingleArgConstructor() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Not found");

            assertThat(ex.getMessage()).isEqualTo("Not found");
        }

        @Test
        @DisplayName("Varargs constructor formats message with args")
        void testVarargsConstructor() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Not found for id :: %s", 42L);

            assertThat(ex.getMessage()).isEqualTo("Not found for id :: 42");
        }

        @Test
        @DisplayName("Varargs constructor handles multiple args")
        void testVarargsConstructorMultiple() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Not found: %s with name: %s", 42L, "test");

            assertThat(ex.getMessage()).isEqualTo("Not found: 42 with name: test");
        }
    }
}
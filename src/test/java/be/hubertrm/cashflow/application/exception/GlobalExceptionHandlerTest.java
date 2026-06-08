package be.hubertrm.cashflow.application.exception;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Nested
    class resourceNotFoundExceptionDesigns {

        @Test
        @DisplayName("resourceNotFoundException returns 404 with ErrorDetails")
        void testResourceNotFoundException() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Account not found :: 1", 1L);
            ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

            ResponseEntity<ErrorDetails> response = handler.resourceNotFoundException(ex, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getMessage()).isEqualTo("Account not found :: 1");
            assertThat(response.getBody().getDetails()).contains("uri=");
        }
    }

    @Nested
    class globalExceptionDesigns {

        @Test
        @DisplayName("globalExceptionHandler returns 500 with ErrorDetails")
        void testGlobalExceptionHandler() {
            Exception ex = new Exception("Something went wrong");
            ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

            ResponseEntity<ErrorDetails> response = handler.globalExceptionHandler(ex, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getMessage()).isEqualTo("Something went wrong");
            assertThat(response.getBody().getTimestamp()).isNotNull();
        }
    }
}
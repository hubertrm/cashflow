package be.hubertrm.cashflow.domain.file.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class DomainExceptionsTest {

    @Nested
    class FieldsNotFoundExceptionDesigns {

        @Test
        @DisplayName("FieldsNotFoundException stores message")
        void testFieldsNotFoundException() {
            FieldsNotFoundException ex = new FieldsNotFoundException("Fields not found");

            assertThat(ex.getMessage()).isEqualTo("Fields not found");
            assertThat(ex).isInstanceOf(Exception.class);
        }
    }

    @Nested
    class FileNotFoundExceptionDesigns {

        @Test
        @DisplayName("FileNotFoundException stores message")
        void testFileNotFoundException() {
            FileNotFoundException ex = new FileNotFoundException("File not found: /path/to/file");

            assertThat(ex.getMessage()).isEqualTo("File not found: /path/to/file");
            assertThat(ex).isInstanceOf(Exception.class);
        }
    }

    @Nested
    class ReadWriteExceptionDesigns {

        @Test
        @DisplayName("ReadWriteException stores message")
        void testReadWriteException() {
            ReadWriteException ex = new ReadWriteException("Read/write error");

            assertThat(ex.getMessage()).isEqualTo("Read/write error");
            assertThat(ex).isInstanceOf(Exception.class);
        }
    }

    @Nested
    class StorageExceptionDesigns {

        @Test
        @DisplayName("StorageException stores message")
        void testStorageException() {
            StorageException ex = new StorageException("Storage error");

            assertThat(ex.getMessage()).isEqualTo("Storage error");
            assertThat(ex).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("StorageException stores message and cause")
        void testStorageExceptionWithCause() {
            Throwable cause = new RuntimeException("root cause");
            StorageException ex = new StorageException("Storage error", cause);

            assertThat(ex.getMessage()).isEqualTo("Storage error");
            assertThat(ex.getCause()).isEqualTo(cause);
        }
    }
}
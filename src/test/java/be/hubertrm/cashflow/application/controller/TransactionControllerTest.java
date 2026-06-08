package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.application.dto.AccountDto;
import be.hubertrm.cashflow.application.dto.CategoryDto;
import be.hubertrm.cashflow.application.dto.TransactionDto;
import be.hubertrm.cashflow.application.manager.TransactionBusinessManager;
import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionControllerTest {

    @Autowired
    private TransactionController controller;

    @MockitoBean
    private TransactionBusinessManager manager;

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);
    private static final CategoryDto CAT = new CategoryDto(1L, "Food", NOW);
    private static final AccountDto ACC = new AccountDto(1L, "Checking", NOW);

    @Nested
    class getAllTransactionsDesigns {

        @Test
        @DisplayName("getAllTransactions returns PagedModel of TransactionDto")
        void testGetAllTransactions() {
            TransactionDto dto = new TransactionDto(1L, NOW, 100f, CAT, ACC, "groceries");
            PageRequest pageable = PageRequest.of(0, 10);
            Page<TransactionDto> page = new PageImpl<>(List.of(dto), pageable, 1);
            when(manager.getAll(pageable)).thenReturn(page);

            var result = controller.getAllTransactions(pageable);

            assertThat(result.getContent()).hasSize(1);
        }
    }

    @Nested
    class getTransactionByIdDesigns {

        @Test
        @DisplayName("getTransactionById returns TransactionDto")
        void testGetTransactionById() throws ResourceNotFoundException {
            TransactionDto dto = new TransactionDto(1L, NOW, 100f, CAT, ACC, "groceries");
            when(manager.getById(1L)).thenReturn(dto);

            TransactionDto result = controller.getTransactionById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }
    }

    @Nested
    class createTransactionDesigns {

        @Test
        @DisplayName("createTransaction returns id")
        void testCreateTransaction() throws ResourceNotFoundException {
            TransactionDto dto = new TransactionDto(null, NOW, 100f, CAT, ACC, "groceries");
            when(manager.create(dto)).thenReturn(1L);

            Long result = controller.createTransaction(dto);

            assertThat(result).isEqualTo(1L);
        }
    }

    @Nested
    class createBulkTransactionsDesigns {

        @Test
        @DisplayName("createBulkTransactions returns list of ids")
        void testCreateBulkTransactions() throws ResourceNotFoundException {
            TransactionDto dto = new TransactionDto(null, NOW, 100f, CAT, ACC, "groceries");
            when(manager.create(List.of(dto))).thenReturn(List.of(1L));

            List<Long> result = controller.createBulkTransactions(List.of(dto));

            assertThat(result).containsExactly(1L);
        }
    }

    @Nested
    class updateTransactionDesigns {

        @Test
        @DisplayName("updateTransaction delegates to manager")
        void testUpdateTransaction() throws ResourceNotFoundException {
            TransactionDto dto = new TransactionDto(1L, NOW, 200f, CAT, ACC, "updated");
            doNothing().when(manager).update(1L, dto);

            assertThatNoException().isThrownBy(() -> controller.updateTransaction(1L, dto));
            verify(manager).update(1L, dto);
        }
    }

    @Nested
    class deleteTransactionByIdDesigns {

        @Test
        @DisplayName("deleteTransactionById delegates to manager")
        void testDeleteTransactionById() throws ResourceNotFoundException {
            doNothing().when(manager).deleteById(1L);

            assertThatNoException().isThrownBy(() -> controller.deleteTransactionById(1L));
            verify(manager).deleteById(1L);
        }
    }

    @Nested
    class evaluateFileDesigns {

        @Test
        @DisplayName("evaluateFile delegates to manager")
        void testEvaluateFile() {
            when(manager.evaluateFileWithHeaders("h1,h2", "v1,v2")).thenReturn(List.of());

            var result = controller.evaluateFile("h1,h2", "v1,v2");

            assertThat(result).isEmpty();
            verify(manager).evaluateFileWithHeaders("h1,h2", "v1,v2");
        }
    }
}
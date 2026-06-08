package be.hubertrm.cashflow.infra.entity;

import be.hubertrm.cashflow.domain.core.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AccountEntityTest {

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);

    @Nested
    class fromDesigns {

        @Test
        @DisplayName("from converts Account to AccountEntity")
        void testFrom() {
            Account account = new Account(1L, "Checking", NOW);

            AccountEntity entity = AccountEntity.from(account);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getName()).isEqualTo("Checking");
            assertThat(entity.getDate()).isEqualTo(NOW);
        }

        @Test
        @DisplayName("from handles null id")
        void testFromNullId() {
            Account account = new Account(null, "NewAccount", NOW);

            AccountEntity entity = AccountEntity.from(account);

            assertThat(entity.getId()).isNull();
            assertThat(entity.getName()).isEqualTo("NewAccount");
        }
    }

    @Nested
    class fromThisDesigns {

        @Test
        @DisplayName("fromThis converts AccountEntity to Account")
        void testFromThis() {
            AccountEntity entity = new AccountEntity(1L, "Checking", NOW);

            Account account = entity.fromThis();

            assertThat(account.getId()).isEqualTo(1L);
            assertThat(account.getName()).isEqualTo("Checking");
            assertThat(account.getDate()).isEqualTo(NOW);
        }
    }

    @Nested
    class constructorDesigns {

        @Test
        @DisplayName("RequiredArgsConstructor creates an entity with default values")
        void testRequiredArgsConstructor() {
            AccountEntity entity = new AccountEntity();
            assertThat(entity.getId()).isNull();
            assertThat(entity.getName()).isNull();
            assertThat(entity.getDate()).isNull();
        }

        @Test
        @DisplayName("AllArgsConstructor creates an entity with all values")
        void testAllArgsConstructor() {
            AccountEntity entity = new AccountEntity(1L, "Savings", NOW);
            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getName()).isEqualTo("Savings");
            assertThat(entity.getDate()).isEqualTo(NOW);
        }

        @Test
        @DisplayName("setters work correctly")
        void testSetters() {
            AccountEntity entity = new AccountEntity();
            entity.setId(2L);
            entity.setName("Investment");
            entity.setDate(NOW);

            assertThat(entity.getId()).isEqualTo(2L);
            assertThat(entity.getName()).isEqualTo("Investment");
            assertThat(entity.getDate()).isEqualTo(NOW);
        }
    }
}
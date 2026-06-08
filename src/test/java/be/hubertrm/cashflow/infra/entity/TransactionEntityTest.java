package be.hubertrm.cashflow.infra.entity;

import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionEntityTest {

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);
    private static final Category CATEGORY = new Category(1L, "Food", NOW);
    private static final Account ACCOUNT = new Account(1L, "Checking", NOW);

    @Nested
    class fromDesigns {

        @Test
        @DisplayName("from converts Transaction to TransactionEntity with all fields set")
        void testFromAllFields() {
            Transaction tx = new Transaction(
                    1L, NOW, 100.50f, CATEGORY, ACCOUNT, "groceries",
                    "Christmas", "December", "AAPL", 10, 5.5f,
                    true, 1.2f, "EUR", 12345L, List.of("tag1", "tag2"));

            TransactionEntity entity = TransactionEntity.from(tx);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getDate()).isEqualTo(NOW);
            assertThat(entity.getAmount()).isEqualTo(100.50f);
            assertThat(entity.getCategory()).isNotNull();
            assertThat(entity.getCategory().getId()).isEqualTo(1L);
            assertThat(entity.getAccount()).isNotNull();
            assertThat(entity.getAccount().getId()).isEqualTo(1L);
            assertThat(entity.getDescription()).isEqualTo("groceries");
            assertThat(entity.getHoliday()).isEqualTo("Christmas");
            assertThat(entity.getMonth()).isEqualTo("December");
            assertThat(entity.getTicker()).isEqualTo("AAPL");
            assertThat(entity.getNbrOfActions()).isEqualTo(10);
            assertThat(entity.getChangeRate()).isEqualTo(5.5f);
            assertThat(entity.getIsCommon()).isTrue();
            assertThat(entity.getBeforeConversion()).isEqualTo(1.2f);
            assertThat(entity.getCurrency()).isEqualTo("EUR");
            assertThat(entity.getReference()).isEqualTo(12345L);
            assertThat(entity.getTags()).containsExactly("tag1", "tag2");
        }

        @Test
        @DisplayName("from handles null category and account")
        void testFromNullCategoryAndAccount() {
            Transaction tx = new Transaction()
                    .setId(1L)
                    .setDate(NOW)
                    .setAmount(100f)
                    .setCategory(null)
                    .setAccount(null)
                    .setDescription("test");

            TransactionEntity entity = TransactionEntity.from(tx);

            assertThat(entity.getCategory()).isNull();
            assertThat(entity.getAccount()).isNull();
        }

        @Test
        @DisplayName("from handles null tags")
        void testFromNullTags() {
            Transaction tx = new Transaction()
                    .setId(1L)
                    .setDate(NOW)
                    .setAmount(100f)
                    .setCategory(CATEGORY)
                    .setAccount(ACCOUNT)
                    .setDescription("test")
                    .setTags(null);

            TransactionEntity entity = TransactionEntity.from(tx);

            assertThat(entity.getTags()).isEmpty();
        }

        @Test
        @DisplayName("from handles null optional fields")
        void testFromMinimalTransaction() {
            Transaction tx = new Transaction(1L, NOW, 100f, CATEGORY, ACCOUNT, "description");

            TransactionEntity entity = TransactionEntity.from(tx);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getDate()).isEqualTo(NOW);
            assertThat(entity.getAmount()).isEqualTo(100f);
            assertThat(entity.getDescription()).isEqualTo("description");
            assertThat(entity.getHoliday()).isNull();
            assertThat(entity.getMonth()).isNull();
            assertThat(entity.getTicker()).isNull();
            assertThat(entity.getNbrOfActions()).isNull();
        }
    }

    @Nested
    class fromThisDesigns {

        @Test
        @DisplayName("fromThis converts TransactionEntity to Transaction with all fields")
        void testFromThisAllFields() {
            CategoryEntity catEntity = CategoryEntity.from(CATEGORY);
            AccountEntity accEntity = AccountEntity.from(ACCOUNT);

            TransactionEntity entity = new TransactionEntity();
            entity.setId(1L);
            entity.setDate(NOW);
            entity.setAmount(100.50f);
            entity.setCategory(catEntity);
            entity.setAccount(accEntity);
            entity.setDescription("groceries");
            entity.setHoliday("Christmas");
            entity.setMonth("December");
            entity.setTicker("AAPL");
            entity.setNbrOfActions(10);
            entity.setChangeRate(5.5f);
            entity.setIsCommon(true);
            entity.setBeforeConversion(1.2f);
            entity.setCurrency("EUR");
            entity.setReference(12345L);
            entity.setTags(List.of("tag1", "tag2"));

            Transaction tx = entity.fromThis();

            assertThat(tx.getId()).isEqualTo(1L);
            assertThat(tx.getDate()).isEqualTo(NOW);
            assertThat(tx.getAmount()).isEqualTo(100.50f);
            assertThat(tx.getCategory()).isNotNull();
            assertThat(tx.getCategory().getId()).isEqualTo(1L);
            assertThat(tx.getAccount()).isNotNull();
            assertThat(tx.getAccount().getId()).isEqualTo(1L);
            assertThat(tx.getDescription()).isEqualTo("groceries");
            assertThat(tx.getHoliday()).isEqualTo("Christmas");
            assertThat(tx.getMonth()).isEqualTo("December");
            assertThat(tx.getTicker()).isEqualTo("AAPL");
            assertThat(tx.getNbrOfActions()).isEqualTo(10);
            assertThat(tx.getChangeRate()).isEqualTo(5.5f);
            assertThat(tx.getIsCommon()).isTrue();
            assertThat(tx.getBeforeConversion()).isEqualTo(1.2f);
            assertThat(tx.getCurrency()).isEqualTo("EUR");
            assertThat(tx.getReference()).isEqualTo(12345L);
            assertThat(tx.getTags()).containsExactly("tag1", "tag2");
        }

        @Test
        @DisplayName("fromThis handles null category and account")
        void testFromThisNullCategoryAndAccount() {
            TransactionEntity entity = new TransactionEntity();
            entity.setId(1L);
            entity.setDate(NOW);
            entity.setAmount(100f);
            entity.setCategory(null);
            entity.setAccount(null);
            entity.setDescription("test");

            Transaction tx = entity.fromThis();

            assertThat(tx.getCategory()).isNull();
            assertThat(tx.getAccount()).isNull();
        }

        @Test
        @DisplayName("fromThis handles null tags")
        void testFromThisNullTags() {
            TransactionEntity entity = new TransactionEntity();
            entity.setId(1L);
            entity.setDate(NOW);
            entity.setAmount(100f);
            entity.setTags(null);

            Transaction tx = entity.fromThis();

            assertThat(tx.getTags()).isEmpty();
        }
    }
}
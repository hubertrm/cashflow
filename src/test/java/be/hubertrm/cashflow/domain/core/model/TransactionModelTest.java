package be.hubertrm.cashflow.domain.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionModelTest {

    private static final LocalDate NOW = LocalDate.of(2021, 12, 31);

    @Nested
    class constructorDesigns {

        @Test
        @DisplayName("6-arg constructor sets all basic fields")
        void testSixArgConstructor() {
            Category category = new Category(1L, "Food", NOW);
            Account account = new Account(1L, "Checking", NOW);
            Transaction tx = new Transaction(1L, NOW, 100.50f, category, account, "groceries");

            assertThat(tx.getId()).isEqualTo(1L);
            assertThat(tx.getDate()).isEqualTo(NOW);
            assertThat(tx.getAmount()).isEqualTo(100.50f);
            assertThat(tx.getCategory()).isEqualTo(category);
            assertThat(tx.getAccount()).isEqualTo(account);
            assertThat(tx.getDescription()).isEqualTo("groceries");
            assertThat(tx.getTags()).isEmpty();
        }

        @Test
        @DisplayName("No-args constructor initializes tags as empty list")
        void testNoArgsConstructor() {
            Transaction tx = new Transaction();

            assertThat(tx.getId()).isNull();
            assertThat(tx.getTags()).isEmpty();
        }

        @Test
        @DisplayName("All-args constructor sets all 16 fields")
        void testAllArgsConstructor() {
            Category category = new Category(1L, "Food", NOW);
            Account account = new Account(1L, "Checking", NOW);
            List<String> tags = List.of("tag1", "tag2");

            Transaction tx = new Transaction(1L, NOW, 100.50f, category, account, "groceries",
                    "Christmas", "December", "AAPL", 10, 5.5f,
                    true, 1.2f, "EUR", 12345L, tags);

            assertThat(tx.getId()).isEqualTo(1L);
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
        @DisplayName("Fluent setters work correctly")
        void testFluentSetters() {
            Transaction tx = new Transaction()
                    .setId(1L)
                    .setDate(NOW)
                    .setAmount(100f)
                    .setDescription("test")
                    .setHoliday("Xmas")
                    .setMonth("Dec")
                    .setTicker("TSLA")
                    .setNbrOfActions(5)
                    .setChangeRate(2.5f)
                    .setIsCommon(false)
                    .setBeforeConversion(0.9f)
                    .setCurrency("USD")
                    .setReference(99L)
                    .setTags(List.of("urgent"));

            assertThat(tx.getId()).isEqualTo(1L);
            assertThat(tx.getHoliday()).isEqualTo("Xmas");
            assertThat(tx.getMonth()).isEqualTo("Dec");
            assertThat(tx.getTicker()).isEqualTo("TSLA");
            assertThat(tx.getNbrOfActions()).isEqualTo(5);
            assertThat(tx.getChangeRate()).isEqualTo(2.5f);
            assertThat(tx.getIsCommon()).isFalse();
            assertThat(tx.getBeforeConversion()).isEqualTo(0.9f);
            assertThat(tx.getCurrency()).isEqualTo("USD");
            assertThat(tx.getReference()).isEqualTo(99L);
            assertThat(tx.getTags()).containsExactly("urgent");
        }
    }
}
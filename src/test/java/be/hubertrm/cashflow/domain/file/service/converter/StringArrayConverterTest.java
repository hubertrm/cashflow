package be.hubertrm.cashflow.domain.file.service.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StringArrayConverterTest {

    private final Converter<String, ArrayList<String>> converter = new StringArrayConverter();

    @Test
    void givenSimpleString_whenConverted_shouldReturnArrayOfSizeOne() {
        String record = "test";

        assertThat(converter.convert(record)).containsOnly(record);
    }

    @Test
    void givenStringSeparatedByDelimiter_whenConverted_shouldReturnArrayOfString() {
        String record = "test,test";
        String[] expectedResult = {"test", "test"};

        assertThat(converter.convert(record)).containsExactlyElementsOf(Arrays.asList(expectedResult));
    }

    @Test
    void givenStringContainsMissingValues_whenConverted_shouldReturnArrayOfStringWithEmptyValues() {
        String record = "test,,test,test";
        String[] expectedResult = {"test", "", "test", "test"};

        assertThat(converter.convert(record)).containsExactlyElementsOf(Arrays.asList(expectedResult));
    }

    @ParameterizedTest
    @MethodSource("provideRecordContainingQuotedField")
    void givenRecordContainsQuotedField_whenConverted_shouldKeepQuotedFieldAsOne(String record, List<String> expectedResult) {
        assertThat(converter.convert(record)).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> provideRecordContainingQuotedField() {
        return Stream.of(
                Arguments.of("\"test,test\"", Collections.singletonList("test,test")),
                Arguments.of("test,\"test,test\"", Arrays.asList("test","test,test")),
                Arguments.of("\"test,test\",test", Arrays.asList("test,test", "test")),
                Arguments.of("test,\"test,test\",test", Arrays.asList("test", "test,test", "test")),
                Arguments.of("test,\"test,test\",\"test,test\",test", Arrays.asList("test", "test,test", "test,test", "test")),
                Arguments.of("test,\"test,test,test", Arrays.asList("test", "\"test", "test", "test"))
        );
    }

}
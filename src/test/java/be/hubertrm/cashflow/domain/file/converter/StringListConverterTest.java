package be.hubertrm.cashflow.domain.file.converter;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class StringListConverterTest {

    private final Converter<String, List<String>> converter = new StringListConverter();

    @Test
    void givenSimpleString_whenConverted_shouldReturnListOfSizeOne() {
        String line = "test";

        Assertions.assertThat(converter.convert(line)).containsOnly(line);
    }

    @Test
    void givenStringSeparatedByDelimiter_whenConverted_shouldReturnListOfString() {
        String line = "test,test";
        List<String> expectedResult = Arrays.asList("test", "test");

        Assertions.assertThat(converter.convert(line)).isEqualTo(expectedResult);
    }

    @Test
    void givenStringContainsMissingValues_whenConverted_shouldReturnListOfStringWithEmptyValues() {
        String line = "test,,test,test";
        List<String> expectedResult = Arrays.asList("test", "", "test", "test");

        Assertions.assertThat(converter.convert(line)).containsExactlyElementsOf(expectedResult);
    }

    @Test
    void givenRecordContainsQuotedField_whenConverted_shouldKeepQuotedFieldAsOne() {
        String line = "\"test,test\"";
        List<String> expectedResult = Collections.singletonList("test,test");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(converter.convert(line)).isEqualTo(expectedResult);

        String line2 = "test,\"test,test\"";
        List<String> expectedResult2 = Arrays.asList("test", "test,test");

        softAssertions.assertThat(converter.convert(line2)).isEqualTo(expectedResult2);

        String line3 = "\"test,test\",test";
        List<String> expectedResult3 = Arrays.asList("test,test", "test");

        softAssertions.assertThat(converter.convert(line3)).isEqualTo(expectedResult3);

        String line4 = "test,\"test,test\",test";
        List<String> expectedResult4 = Arrays.asList("test", "test,test", "test");

        softAssertions.assertThat(converter.convert(line4)).isEqualTo(expectedResult4);

        String line5 = "test,\"test,test\",\"test,test\",test";
        List<String> expectedResult5 = Arrays.asList("test", "test,test", "test,test", "test");

        softAssertions.assertThat(converter.convert(line5)).isEqualTo(expectedResult5);

        String line6 = "test,\"test,test,test";
        List<String> expectedResult6 = Arrays.asList("test", "test", "test", "test");

        softAssertions.assertThat(converter.convert(line6)).isEqualTo(expectedResult6);
        softAssertions.assertAll();
    }

    @Test
    void givenMultipleStringSeparatedByDelimiter_whenConverted_shouldReturnCollectionOfListOfString() {
        List<String> lines = Arrays.asList("test,test", "test,test");
        List<List<String>> expectedResult = Arrays.asList(Arrays.asList("test", "test"), Arrays.asList("test", "test"));

        Assertions.assertThat(converter.convert(lines)).isEqualTo(expectedResult);
    }

}
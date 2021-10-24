package be.hubertrm.cashflow.facade.file.factory;

import be.hubertrm.cashflow.facade.file.converter.Converter;
import be.hubertrm.cashflow.facade.file.converter.StringArrayConverter;
import be.hubertrm.cashflow.facade.file.converter.StringListConverter;
import be.hubertrm.cashflow.facade.file.converter.enums.OutputType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConverterFactoryTest {

    private ConverterFactory converterFactory;

    @BeforeEach
    void setup() {
        StringArrayConverter stringArrayConverter = new StringArrayConverter();
        StringListConverter stringListConverter = new StringListConverter();
        Set<Converter<String, ? extends List<String>>> converterSet = Set.of(stringArrayConverter, stringListConverter);
        converterFactory = new ConverterFactory(converterSet);
    }

    @Test
    void givenNoMarker_shouldReturnStringToArrayConverter() {
        assertThat(converterFactory.createConverter(OutputType.ARRAY)).isInstanceOf(StringArrayConverter.class);
    }

    @Test
    void givenStringMarker_shouldReturnStringToListConverter() {
        assertThat(converterFactory.createConverter(OutputType.LIST)).isInstanceOf(StringListConverter.class);
    }
}
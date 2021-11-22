package be.hubertrm.cashflow.domain.file.service.factory;

import be.hubertrm.cashflow.domain.file.service.converter.Converter;
import be.hubertrm.cashflow.domain.file.service.converter.StringArrayConverter;
import be.hubertrm.cashflow.domain.file.service.converter.StringListConverter;
import be.hubertrm.cashflow.domain.file.service.converter.enums.OutputType;
import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(converterFactory.createConverter(OutputType.ARRAY)).isInstanceOf(StringArrayConverter.class);
    }

    @Test
    void givenStringMarker_shouldReturnStringToListConverter() {
        Assertions.assertThat(converterFactory.createConverter(OutputType.LIST)).isInstanceOf(StringListConverter.class);
    }
}
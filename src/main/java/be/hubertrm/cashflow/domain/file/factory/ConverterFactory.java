package be.hubertrm.cashflow.domain.file.factory;

import be.hubertrm.cashflow.domain.file.converter.Converter;
import be.hubertrm.cashflow.domain.file.converter.enums.OutputType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConverterFactory {

    private Map<OutputType, Converter<String, ? extends List<String>>> converterServices;

    public ConverterFactory(Set<Converter<String, ? extends List<String>>> converterSet) {
        populateConverterServices(converterSet);
    }

    public Converter<String, List<String>> createConverter(OutputType type) {
        return (Converter<String, List<String>>) converterServices.get(type);
    }

    private void populateConverterServices(Set<Converter<String, ? extends List<String>>> converterSet) {
        converterServices = converterSet.stream().collect(Collectors
                .<Converter<String, ? extends List<String>>, OutputType, Converter<String, ? extends List<String>>>toMap(Converter::getSupportedOutputType, converter -> converter));
    }
}

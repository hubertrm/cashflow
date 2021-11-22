package be.hubertrm.cashflow.domain.file.service.converter;

import be.hubertrm.cashflow.domain.file.service.converter.enums.OutputType;

import java.util.Collection;

/**
 * {@code Converter} class represents set of actions of conversion
 * @version 0.1
 * @author Hubert Romain - hubertrm
 * @param <T> the type of the data to be converted
 * @param <S> the type of the converted data
 */
public interface Converter<T, S> {

    String DELIMITER = ",";
    String MARKER = "\"";

    OutputType getSupportedOutputType();

    S convert(T value);

    Collection<S> convert(Collection<T> values);
}

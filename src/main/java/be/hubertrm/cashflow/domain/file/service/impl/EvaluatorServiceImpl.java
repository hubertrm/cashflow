package be.hubertrm.cashflow.domain.file.service.impl;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.service.AccountService;
import be.hubertrm.cashflow.domain.core.service.CategoryService;
import be.hubertrm.cashflow.domain.file.converter.Converter;
import be.hubertrm.cashflow.domain.file.enums.DatePattern;
import be.hubertrm.cashflow.domain.file.model.Error;
import be.hubertrm.cashflow.domain.file.model.Evaluation;
import be.hubertrm.cashflow.domain.file.model.RecordEvaluated;
import be.hubertrm.cashflow.domain.file.model.RecordField;
import be.hubertrm.cashflow.domain.file.service.EvaluatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static be.hubertrm.cashflow.domain.file.enums.ErrorType.*;

@Slf4j
@Service
public class EvaluatorServiceImpl implements EvaluatorService {

    private DateTimeFormatter dateTimeFormatter;

    @Resource
    public CategoryService categoryService;
    @Resource
    public AccountService accountService;

    @Resource
    Converter<String, List<String>> converter;

    @Override
    public RecordEvaluated create(String[] fields, String line) {
        return this.create(fields, line, Locale.ROOT);
    }

    @Override
    public RecordEvaluated create(String[] fields, String line, Locale locale) {
        var recordEvaluated = new RecordEvaluated();
        try {
            List<String> elements = converter.convert(line);
            Map<String, RecordField> fieldsMap = getValueRecordFieldMap(locale);
            for (var i = 0; i < fields.length; i++) {
                switch (fieldsMap.getOrDefault(fields[i], RecordField.NOT_SUPPORTED)) {
                    case DATE:
                        recordEvaluated.setDate(mapDate(elements.get(i)));
                        break;
                    case PRICE:
                        recordEvaluated.setAmount(mapPrice(elements.get(i)));
                        break;
                    case CATEGORY:
                        recordEvaluated.setCategory(mapCategory(elements.get(i)));
                        break;
                    case ACCOUNT:
                        recordEvaluated.setAccount(mapAccount(elements.get(i)));
                        break;
                    case DESCRIPTION:
                        recordEvaluated.setDescription(mapDescription(elements.get(i)));
                        break;
                    case NOT_SUPPORTED:
                    default:
                        log.debug("Field \"{}\" is not supported (element: {}, value: {})", fields[i], i, elements.get(i));
                }
            }
        } catch (Exception e) {
            log.debug("Could not create Record from line {}.", line, e);
            return null;
        }
        return recordEvaluated;
    }

    public void setDateTimeFormatter(String pattern) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    private Map<String, RecordField> getValueRecordFieldMap(Locale locale) {
        var resource = ResourceBundle.getBundle("record-fields", locale);
        return resource.keySet().stream()
                .collect(Collectors.toMap(resource::getString, this::getMatchingRecordField));
    }

    private RecordField getMatchingRecordField(String value) {
        return Arrays.stream(RecordField.values())
                .filter(recordField -> recordField.getValue().equals(value))
                .findFirst()
                .orElse(RecordField.NOT_SUPPORTED);
    }

    private Evaluation mapDate(String date) {
        Evaluation evaluation = new Evaluation().setValue(date);
        try {
            if (dateTimeFormatter == null) {
                dateTimeFormatter = DateTimeFormatter.ofPattern(getDatePattern(DatePattern.DEFAULT_DATE_PATTERN));
            }
            return evaluation.setValue(LocalDate.parse(date, dateTimeFormatter).toString());
        } catch (DateTimeParseException e) {
            Error error = new Error(WRONG_FORMAT, "The date format is wrong. Accepted date format is: " + dateTimeFormatter);
            return evaluation.setError(error);
        } catch (NullPointerException e) {
            Error error = new Error(MISSING_VALUE, "The date is missing.");
            return evaluation.setError(error);
        }
    }

    private static String getDatePattern(DatePattern datePattern) {
        var resources = ResourceBundle.getBundle("record-files-supported-date-format");
        return resources.getString(datePattern.getValue());
    }

    private Evaluation mapPrice(String amount) {
        var amountPattern = "^\\s*([+-]?\\d+(,\\d+)?)\\s*€?";
        var pattern = Pattern.compile(amountPattern);
        var matcher = pattern.matcher(amount);
        if (matcher.find()) {
            return new Evaluation().setValue((matcher.group(1).replace(',', '.')));
        }
        Error error = new Error(WRONG_FORMAT, "The price format is wrong.");
        return new Evaluation().setValue(amount).setError(error);
    }

    private Evaluation mapCategory(String categoryName) {
        try {
            return new Evaluation().setValue(categoryService.getByName(categoryName).getName());
        } catch (ResourceNotFoundException e) {
            Error error = new Error(FIELD_DOES_NOT_EXIST, "This field does not exists");
            return new Evaluation().setValue(categoryName).setError(error);
        }
    }

    private Evaluation mapAccount(String sourceName) {
        try {
            return new Evaluation().setValue(accountService.getByName(sourceName).getName());
        } catch (ResourceNotFoundException e) {
            Error error = new Error(FIELD_DOES_NOT_EXIST, "This field does not exists");
            return new Evaluation().setValue(sourceName).setError(error);
        }
    }

    private Evaluation mapDescription(String description) {
        return new Evaluation().setValue(description);
    }
}

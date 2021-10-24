package be.hubertrm.cashflow.facade.file.service.impl;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.service.AccountService;
import be.hubertrm.cashflow.domain.service.CategoryService;
import be.hubertrm.cashflow.facade.dto.AccountDto;
import be.hubertrm.cashflow.facade.dto.CategoryDto;
import be.hubertrm.cashflow.facade.dto.TransactionDto;
import be.hubertrm.cashflow.facade.file.converter.Converter;
import be.hubertrm.cashflow.facade.file.enums.DatePattern;
import be.hubertrm.cashflow.facade.file.model.RecordField;
import be.hubertrm.cashflow.facade.file.service.TransactionEvaluatorService;
import be.hubertrm.cashflow.facade.mapper.AccountMapper;
import be.hubertrm.cashflow.facade.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionEvaluatorServiceImpl implements TransactionEvaluatorService {

    public final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
    public final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    private DateTimeFormatter dateTimeFormatter;

    @Resource
    public CategoryService categoryService;
    @Resource
    public AccountService accountService;

    @Resource
    Converter<String, List<String>> converter;

    @Override
    public TransactionDto create(String[] fields, String line) {
        return this.create(fields, line, Locale.ROOT);
    }

    @Override
    public TransactionDto create(String[] fields, String line, Locale locale) {
        var transactionDto = new TransactionDto();
        try {
            List<String> elements = converter.convert(line);
            Map<String, RecordField> fieldsMap = getValueRecordFieldMap(locale);
            for (var i = 0; i < fields.length; i++) {
                switch (fieldsMap.getOrDefault(fields[i], RecordField.NOT_SUPPORTED)) {
                    case DATE:
                        transactionDto.setDate(mapDate(elements.get(i)));
                        break;
                    case PRICE:
                        transactionDto.setAmount(mapAmount(elements.get(i)));
                        break;
                    case CATEGORY:
                        transactionDto.setCategory(mapCategory(elements.get(i)));
                        break;
                    case ACCOUNT:
                        transactionDto.setAccount(mapAccount(elements.get(i)));
                        break;
                    case DESCRIPTION:
                        transactionDto.setDescription(elements.get(i));
                        break;
                    case NOT_SUPPORTED:
                    default:
                        log.debug("Field \"{}\" is not supported (element: {}, value: {})", fields[i], i, elements.get(i));
                }
            }
        } catch (ResourceNotFoundException e) {
            log.debug("Could not create Record from line {}.", line, e);
            return null;
        }
        return transactionDto;
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

    private LocalDate mapDate(String date) {
        if (dateTimeFormatter != null) {
            return date != null ? LocalDate.parse(date, dateTimeFormatter) : null;
        }
        var datePattern = DatePattern.DEFAULT_DATE_PATTERN;
        return date != null ? LocalDate.parse(date, DateTimeFormatter.ofPattern(getDatePattern(datePattern)))  : null;
    }

    private static String getDatePattern(DatePattern datePattern) {
        var resources = ResourceBundle.getBundle("record-files-supported-date-format");
        return resources.getString(datePattern.getValue());
    }

    private Float mapAmount(String amount) {
        var amountPattern = "^\\s*(\\d+(,\\d+)?)\\s*€";
        var pattern = Pattern.compile(amountPattern);
        var matcher = pattern.matcher(amount);
        return matcher.find() ? Float.parseFloat(matcher.group(1).replace(',', '.')) : 0.0f;
    }

    private CategoryDto mapCategory(String categoryName) throws ResourceNotFoundException {
        return categoryMapper.toDto(categoryService.getByName(categoryName));
    }

    private AccountDto mapAccount(String sourceName) throws ResourceNotFoundException {
        return accountMapper.toDto(accountService.getByName(sourceName));
    }
}

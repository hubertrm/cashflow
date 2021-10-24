package be.hubertrm.cashflow.domain.file.service;

import be.hubertrm.cashflow.facade.dto.TransactionDto;

import java.util.Locale;

public interface TransactionEvaluatorService {

    TransactionDto create(String[] fields, String line);

    TransactionDto create(String[] fields, String line, Locale locale);
}

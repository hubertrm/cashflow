package be.hubertrm.cashflow.domain.file.service;

import be.hubertrm.cashflow.domain.file.model.RecordEvaluated;

import java.util.Locale;

public interface EvaluatorService {

    RecordEvaluated create(String[] fields, String line);

    RecordEvaluated create(String[] fields, String line, Locale locale);
}

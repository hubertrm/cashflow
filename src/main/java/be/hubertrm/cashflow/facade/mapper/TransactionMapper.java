package be.hubertrm.cashflow.facade.mapper;

import be.hubertrm.cashflow.domain.core.model.Transaction;
import be.hubertrm.cashflow.facade.dto.TransactionDto;
import org.mapstruct.Mapper;

/**
 * The abstract class TransactionMapper provides the methods for mapping Transaction to TransactionDto and back.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper extends GenericMapper<Transaction, TransactionDto> {
}

package be.hubertrm.cashflow.application.mapper;

import be.hubertrm.cashflow.domain.core.model.Account;
import be.hubertrm.cashflow.application.dto.AccountDto;
import org.mapstruct.Mapper;

/**
 * The Interface SourceMapper provides the methods for mapping Source to SourceDto and back.
 */
@Mapper
public interface AccountMapper extends GenericMapper<Account, AccountDto> {
}

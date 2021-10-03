package be.hubertrm.cashflow.facade.mapper;

import be.hubertrm.cashflow.domain.model.Account;
import be.hubertrm.cashflow.facade.dto.AccountDto;
import org.mapstruct.Mapper;

/**
 * The Interface SourceMapper provides the methods for mapping Source to SourceDto and back.
 */
@Mapper
public interface AccountMapper extends GenericMapper<Account, AccountDto> {
}

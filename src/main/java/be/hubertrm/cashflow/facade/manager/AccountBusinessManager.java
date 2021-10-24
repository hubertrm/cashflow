package be.hubertrm.cashflow.facade.manager;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.service.AccountService;
import be.hubertrm.cashflow.facade.dto.AccountDto;
import be.hubertrm.cashflow.facade.mapper.AccountMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Component
public class AccountBusinessManager {

    @Resource
    private AccountService accountService;

    private final AccountMapper sourceMapper = Mappers.getMapper(AccountMapper.class);

    public List<AccountDto> getAllAccounts() {
        return sourceMapper.toDtoList(accountService.getAll());
    }

    public AccountDto getAccountById(Long id) throws ResourceNotFoundException {
        return sourceMapper.toDto(accountService.getById(id));
    }

    public Long createAccount(AccountDto accountDto) {
        setDateIfNotSpecified(accountDto);
        return accountService.create(sourceMapper.toModel(accountDto));
    }

    public void updateAccount(Long id, AccountDto accountDto) throws ResourceNotFoundException {
        setDateIfNotSpecified(accountDto);
        accountService.update(id, sourceMapper.toModel(accountDto));
    }
    
    public void deleteAccountById(Long id) throws ResourceNotFoundException {
        accountService.deleteById(id);
    }

    private void setDateIfNotSpecified(AccountDto accountDto) {
        if(accountDto.getDate() == null) {
            accountDto.setDate(LocalDate.now());
        }
    }
}

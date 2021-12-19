package be.hubertrm.cashflow.application.manager;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.core.service.AccountService;
import be.hubertrm.cashflow.application.dto.AccountDto;
import be.hubertrm.cashflow.application.mapper.AccountMapper;
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

    public List<Long> createBulkAccounts(List<AccountDto> accountDtoList) {
        setDateIfNotSpecified(accountDtoList);
        return accountService.createAll(sourceMapper.toModelList(accountDtoList));
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

    private void setDateIfNotSpecified(List<AccountDto> accountDtoList) {
        accountDtoList.forEach(accountDto -> {
            if(accountDto.getDate() == null) {
                accountDto.setDate(LocalDate.now());
            }
        });
    }
}

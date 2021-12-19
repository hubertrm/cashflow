package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.domain.core.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.application.dto.AccountDto;
import be.hubertrm.cashflow.application.manager.AccountBusinessManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Resource
    private AccountBusinessManager sourceBusinessManager;

    @GetMapping("")
    public List<AccountDto> getAllAccounts() {
        return sourceBusinessManager.getAllAccounts();
    }

    @GetMapping("/{id}")
    public AccountDto getAccountById(@PathVariable(value = "id") Long sourceId)
            throws ResourceNotFoundException {
        return sourceBusinessManager.getAccountById(sourceId);
    }

    @PostMapping("")
    public Long createAccount(@RequestBody AccountDto sourceDto) {
        return sourceBusinessManager.createAccount(sourceDto);
    }

    @PostMapping("/bulk")
    public List<Long> createBulkAccounts(@RequestBody List<AccountDto> sourceDtoList) {
        return sourceBusinessManager.createBulkAccounts(sourceDtoList);
    }

    @PutMapping("/{id}")
    public void updateAccount(@PathVariable(value = "id") Long accountId, @RequestBody AccountDto accountDto)
            throws ResourceNotFoundException {
        sourceBusinessManager.updateAccount(accountId, accountDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAccountById(@PathVariable(value = "id") Long sourceId)
            throws ResourceNotFoundException {
        sourceBusinessManager.deleteAccountById(sourceId);
    }
}

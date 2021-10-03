package be.hubertrm.cashflow.facade.controller;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.facade.dto.TransactionDto;
import be.hubertrm.cashflow.facade.manager.TransactionBusinessManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Resource
    private TransactionBusinessManager transactionBusinessManager;

    @GetMapping("")
    public List<TransactionDto> getAllTransactions() {
        return transactionBusinessManager.getAll();
    }

    @GetMapping("/{id}")
    public TransactionDto getTransactionById(@PathVariable(value = "id") Long transactionId)
            throws ResourceNotFoundException {
        return transactionBusinessManager.getById(transactionId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createTransaction(@RequestBody TransactionDto transactionDto)
            throws ResourceNotFoundException {
        return transactionBusinessManager.create(transactionDto);
    }

    @PostMapping("/bulk")
    public List<Long> createBulkTransactions(@RequestBody List<TransactionDto> transactionDtoList) {
        return transactionBusinessManager.create(transactionDtoList);
    }

    @PutMapping("/{id}")
    public void updateTransaction(@PathVariable(value = "id") Long transactionId,
                                  @RequestBody TransactionDto transactionDto) throws ResourceNotFoundException {
        transactionBusinessManager.update(transactionId, transactionDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTransactionById(@PathVariable(value = "id") Long transactionId) throws ResourceNotFoundException {
        transactionBusinessManager.deleteById(transactionId);
    }
}

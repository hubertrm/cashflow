package be.hubertrm.cashflow.facade.manager;

import be.hubertrm.cashflow.domain.exception.ResourceNotFoundException;
import be.hubertrm.cashflow.domain.service.AccountService;
import be.hubertrm.cashflow.domain.service.CategoryService;
import be.hubertrm.cashflow.domain.service.TransactionService;
import be.hubertrm.cashflow.facade.dto.TransactionDto;
import be.hubertrm.cashflow.facade.mapper.TransactionMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TransactionBusinessManager {

    @Resource
    private TransactionService transactionService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private AccountService accountService;

    private final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);

    public List<TransactionDto> getAll() {
        return transactionMapper.toDtoList(transactionService.getAll());
    }

    public TransactionDto getById(Long id) throws ResourceNotFoundException {
        return transactionMapper.toDto(transactionService.getById(id));
    }

    public Long create(TransactionDto dto) throws ResourceNotFoundException {
        assertCategoryAndAccountExists(dto);
        return transactionService.create(transactionMapper.toModel(dto));
    }

    public List<Long> create(List<TransactionDto> dtoList) {
        return transactionService.create(transactionMapper.toModelList(dtoList));
    }

    public void update(Long id, TransactionDto dto) throws ResourceNotFoundException {
        transactionService.update(id, transactionMapper.toModel(dto));
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        transactionService.deleteById(id);
    }

    private void assertCategoryAndAccountExists(TransactionDto dto) throws ResourceNotFoundException {
        if (dto.getCategory().getId() == null || !categoryService.exists(dto.getCategory().getId())) {
            throw new ResourceNotFoundException("Cannot find Category with id :: " + dto.getCategory().getId());
        } else if (dto.getAccount().getId() == null || !accountService.exists(dto.getAccount().getId())) {
            throw new ResourceNotFoundException("Cannot find Account with id :: " + dto.getAccount().getId());
        }
    }
}
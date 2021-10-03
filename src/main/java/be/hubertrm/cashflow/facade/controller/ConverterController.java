package be.hubertrm.cashflow.facade.controller;

import be.hubertrm.cashflow.facade.dto.TransactionDto;
import be.hubertrm.cashflow.facade.manager.TransactionBusinessManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/converter")
public class ConverterController {

    @Resource
    private TransactionBusinessManager transactionBusinessManager;

    @PostMapping("")
    public List<TransactionDto> convertTransactions(@RequestBody String transactions) {
        return null;
    }
}

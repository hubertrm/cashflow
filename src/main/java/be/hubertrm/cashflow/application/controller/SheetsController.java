package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.application.dto.RangeDto;
import be.hubertrm.cashflow.application.mapper.RangeMapper;
import be.hubertrm.cashflow.domain.sheets.entity.RangeOptions;
import be.hubertrm.cashflow.domain.sheets.service.SheetsService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/sheets")
public class SheetsController {

    @Value("${cashflow.sheets.default.majorDimension:ROWS}")
    private String defaultMajorDimension;

    @Value("${cashflow.sheets.default.valueRenderOption:UNFORMATTED_VALUE}")
    private String defaultValueRenderOption;

    @Resource
    private SheetsService sheetsService;

    @GetMapping("/{id}")
    public ResponseEntity<RangeDto> getBySpreadSheetId(@PathVariable(value = "id") String id,
                                                       @RequestParam(value = "range") String range,
                                                       @RequestParam(value = "majorDimension", required = false) String majorDimension,
                                                       @RequestParam(value = "valueRenderOption", required = false) String valueRenderOption
    ) {
        majorDimension = majorDimension != null ? majorDimension : defaultMajorDimension;
        valueRenderOption = valueRenderOption != null ? valueRenderOption : defaultValueRenderOption;

        RangeDto result = RangeMapper.toRangeDto(sheetsService
                .getRangeBySpreadSheetIdRangeAndValueRenderOption(id, range, new RangeOptions(majorDimension, valueRenderOption)));
        return ResponseEntity.ok(result);
    }
}

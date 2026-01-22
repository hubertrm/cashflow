package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.application.dto.SyncRequestDto;
import be.hubertrm.cashflow.application.dto.SyncResultDto;
import be.hubertrm.cashflow.application.manager.SyncBusinessManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/sync")
public class SyncController {

    @Resource
    private SyncBusinessManager syncBusinessManager;

    @PostMapping("/sheets")
    public ResponseEntity<SyncResultDto> syncFromSheets(@RequestBody SyncRequestDto request) {
        SyncResultDto result = syncBusinessManager.syncSheetToDatabase(request);
        return ResponseEntity.ok(result);
    }
}

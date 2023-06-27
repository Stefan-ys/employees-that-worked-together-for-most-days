package com.example.employees.web;

import com.example.employees.entities.Pair;
import com.example.employees.service.CSVService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class CSVController {
    private final CSVService csvService;

    public CSVController(CSVService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/process-csv")
    public ResponseEntity<List<Pair>> processCSV(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("dateFormat") String dateFormat) {
        try {
            List<Pair> result = csvService.processCSV(file,dateFormat);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}

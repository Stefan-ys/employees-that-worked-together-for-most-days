package com.example.employees.web;

import com.example.employees.entity.Pair;
import com.example.employees.service.CSVService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RestController
public class CSVController {
    private final CSVService csvService;

    public CSVController(CSVService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/process-csv")
    public ResponseEntity<List<Pair>> processCSV(@RequestParam("file") MultipartFile file) {
        try {
            List<Pair> result = csvService.processCSV(file);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}

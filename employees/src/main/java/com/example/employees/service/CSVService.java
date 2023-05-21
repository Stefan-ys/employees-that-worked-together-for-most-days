package com.example.employees.service;

import com.example.employees.entities.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CSVService {
    List<Pair> processCSV(MultipartFile file);
}

package com.example.employees;

import com.example.employees.entities.Pair;
import com.example.employees.service.CSVServiceImpl;
import com.example.employees.web.CSVController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CSVControllerTest {

    @Mock
    private CSVServiceImpl csvService;

    private CSVController csvController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        csvController = new CSVController(csvService);
    }

    @Test
    public void testProcessSCVValid() {
        List<Pair> expectedResult = new ArrayList<>();
        when(csvService.processCSV(any(MultipartFile.class))).thenReturn(expectedResult);

        MultipartFile file = new MockMultipartFile("test.csv", new byte[0]);

        ResponseEntity<List<Pair>> response = csvController.processCSV(file);

        verify(csvService, times(1)).processCSV(any(MultipartFile.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    public void testProcessCSVInvalid() {
        when(csvService.processCSV(any(MultipartFile.class))).thenThrow(new RuntimeException("Error"));

        MultipartFile file = new MockMultipartFile("test.csv", new byte[0]);

        ResponseEntity<List<Pair>> response = csvController.processCSV(file);

        verify(csvService, times(1)).processCSV(any(MultipartFile.class));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}

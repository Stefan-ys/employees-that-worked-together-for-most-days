package com.example.employees.configuration;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

@Component
public class DateParser {
    private static final String[] DATE_FORMATS = {
            "yyyy-MM-dd",
            "MM/dd/yy",
            "MM/dd/yyyy",
            "dd/MM/yyyy",
            "MMM dd, yyyy",
            "EEEE, MMMM dd, yyyy",
            "MMMM dd, yyyy",
            "dd-MMM-yyyy",
            "yyyy/MM/dd",
            "dd-MM-yyyy",
            "MMM dd yyyy",
            "dd/MMM/yyyy",
            "yyyy-MMM-dd",
            "MM/dd/yyyy HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "dd/MM/yyyy HH:mm:ss",
            "MMM dd, yyyy HH:mm:ss",
            "EEEE, MMMM dd, yyyy HH:mm:ss",
            "MMMM dd, yyyy HH:mm:ss",
            "dd-MMM-yyyy HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "dd-MM-yyyy HH:mm:ss",
            "MMM dd yyyy HH:mm:ss",
            "dd/MMM/yyyy HH:mm:ss",
            "yyyy-MMM-dd HH:mm:ss"
    };

    public LocalDate parseDate(String input) {
        DateTimeFormatter formatter;
        for (String format : DATE_FORMATS) {
            formatter = DateTimeFormatter.ofPattern(format);

            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                 // Try next format
            }
        } 
        throw new IllegalArgumentException("Invalid date format: " + input);
    }
}

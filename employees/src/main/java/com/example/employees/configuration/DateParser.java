package com.example.employees.configuration;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

@Component
public class DateParser {
    private static final List<String> DATE_FORMATS = Arrays.asList(
            "yyyy-MM-dd",
            "MM/dd/yyyy",
            "dd/MM/yyyy",
            "MMM dd, yyyy",
            "EEEE, MMMM dd, yyyy"
    );

    public LocalDate parseDate(String input) {
        DateTimeFormatter formatter;
        for (String format : DATE_FORMATS) {
            formatter = DateTimeFormatter.ofPattern(format);

            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                 // This is for the loop
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + input);

    }
}

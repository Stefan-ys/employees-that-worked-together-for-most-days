package com.example.employees.components;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Component
public class DateParser {
    private static final String[] DATE_FORMATS = {
            "yyyy-MM-dd",
            "dd-MM-yyyy",
            "MM-dd-yyyy",
            "yyyy-M-d",
            "MM-dd-yy",
            "dd-MM-yy",
            "MMM-dd-yyyy",
            "MMMM-dd-yyyy",
            "dd-MMM-yyyy",
    };

    public LocalDate parseDate(String input) {

        input = prepareInput(input);

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

    private String prepareInput(String input) {
        String regexTime = "\\d{2}:\\d{2}(:\\d{2})?";
        String regexDayOfTheWeek = "\\b\\w+day\\b";

        input = input.replaceAll(regexTime, "");
        input = input.replaceAll(regexDayOfTheWeek, "");
        input = input.replaceAll(",", "").trim();
        input = input.replaceAll("[/ ]", "-");

        return input;
    }
}

package com.example.employees.components;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.StringMultipartFileEditor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Component
public class DateParser {
    private static final String[] DATE_FORMATS_DMY = {
            "dd-MM-yy",
            "dd-MM-yyyy",
            "dd-MMM-yy",
            "dd-MMM-yyyy",
            "dd-MMMM-yy",
            "dd-MMMM-yyyy",
    };

    private static final String[] DATE_FORMATS_MDY = {
            "MM-dd-yy",
            "MM-dd-yyyy",
            "MMM-dd-yy",
            "MMM-dd-yyyy",
            "MMMM-dd-yy",
            "MMMM-dd-yyyy",
    };

    private static final String[] DATE_FORMATS_YMD = {
            "yy-MM-dd",
            "yyyy-MM-dd",
            "yy-MMM-dd",
            "yyyy-MMM-dd",
            "yy-MMMM-dd",
            "yyyy-MMMM-dd",
    };
    private static final String[] DATE_FORMATS_YDM = {
            "yy-dd-MM",
            "yyyy-dd-MM",
            "yy-dd-MMM",
            "yyyy-dd-MMM",
            "yy-dd-MMMM",
            "yyyy-dd-MMMM",
    };


    public LocalDate parseDate(String input, String dateFormat) {

        input = prepareInput(input);

        DateTimeFormatter formatter;

        String[] dateFormatsArr = dateFormat.equals("DMY") ? DATE_FORMATS_DMY :
                dateFormat.equals("MDY") ? DATE_FORMATS_MDY :
                        dateFormat.equals("YMD") ? DATE_FORMATS_YMD : DATE_FORMATS_YDM;

        for (String format : dateFormatsArr) {
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
        String regexToRemove = "(" + regexTime + "|" + regexDayOfTheWeek + "|,)";

        input = input.replaceAll(regexToRemove, "").trim();

        String[] arr = input.split("[./ -]");

        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length() == 1) {
                arr[i] = "0" + arr[i];
            }
        }

        return String.join("-", arr);
    }
}

package com.example.employees;

import com.example.employees.configuration.DateParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class DateParserTest {
    @InjectMocks
    private DateParser dateParser;

    @Test
    public void testParseDateValid() {
        Assertions.assertEquals(LocalDate.of(2022, 12, 31), dateParser.parseDate("2022-12-31"));
        Assertions.assertEquals(LocalDate.of(2023, 1, 15), dateParser.parseDate("01/15/2023"));
        Assertions.assertEquals(LocalDate.of(2023, 6, 30), dateParser.parseDate("30/06/2023"));
        Assertions.assertEquals(LocalDate.of(2022, 12, 31), dateParser.parseDate("Dec 31, 2022"));
        Assertions.assertEquals(LocalDate.of(2023, 1, 15), dateParser.parseDate("January 15, 2023"));
        Assertions.assertEquals(LocalDate.of(2023, 6, 30), dateParser.parseDate("Friday, June 30, 2023"));
    }

    @Test
    public void testParseDateInvalid(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> dateParser.parseDate("2022/12/31"));
    }


}

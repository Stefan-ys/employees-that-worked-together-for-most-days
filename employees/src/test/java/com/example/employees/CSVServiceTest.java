package com.example.employees;

import com.example.employees.components.DateParser;
import com.example.employees.entities.Pair;
import com.example.employees.service.CSVService;
import com.example.employees.service.CSVServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;


import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CSVServiceTest {
    @InjectMocks
    private CSVService csvService = new CSVServiceImpl(new DateParser());

    @Test
    public void testProcessCSVWithSingleProject() {

        String csvContent = """
                143, 12, 2013-11-01, 2014-02-20
                144, 12, 2015-02-10, 2015-03-20
                145, 12, 2014-02-05, 2014-02-15""";

        MockMultipartFile file = new MockMultipartFile("file.csv", csvContent.getBytes());
        String dateFormat = "YMD";

        List<Pair> result = csvService.processCSV(file, dateFormat);

        Assertions.assertEquals(1, result.size());

        Pair expectedPair = new Pair(143, 145, 10);
        expectedPair.getProjects().put(12, 10);
        Assertions.assertEquals(1, result.size());

        Assertions.assertEquals(expectedPair.getEmployeeOneId(), result.get(0).getEmployeeOneId());
        Assertions.assertEquals(expectedPair.getEmployeeTwoId(), result.get(0).getEmployeeTwoId());
        Assertions.assertEquals(expectedPair.getDaysWorkedTogether(), result.get(0).getDaysWorkedTogether());

        Assertions.assertTrue(result.get(0).getProjects().containsKey(12));
        Assertions.assertEquals(expectedPair.getProjects().get(12), (int) result.get(0).getProjects().get(12));
    }

    @Test
    public void testProcessCSVWithMultiProjects() {
        String csvContent = """
                1,1,2019-7-4,2020-8-14
                1,2,2019-12-25,2020-12-28
                1,3,2018-10-12,NULL
                1,4,2019-11-16,NULL
                1,5,2020-1-5,2020-12-21
                2,1,2018-10-3,NULL
                2,2,2019-1-16,2020-3-24
                2,3,2019-5-22,2019-12-26
                2,4,2020-3-7,NULL
                2,5,2018-1-24,2019-1-15
                3,1,2019-3-21,2020-11-26
                3,5,2019-9-28,2020-12-25
                4,2,2018-10-22,NULL
                4,3,2018-1-27,2020-8-28
                5,3,2018-2-3,2020-10-14
                5,5,2018-8-4,NULL""";

        MockMultipartFile file = new MockMultipartFile("file.csv", csvContent.getBytes());
        String dateFormat = "YMD";
        List<Pair> result = csvService.processCSV(file, dateFormat);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(4, result.get(0).getProjects().size());
        Assertions.assertEquals(1, result.get(0).getEmployeeOneId());
        Assertions.assertEquals(2, result.get(0).getEmployeeTwoId());

        for (int i = 0; i < result.get(0).getProjects().size(); i++) {
            Assertions.assertTrue(result.get(0).getProjects().containsKey(i + 1));
        }
    }

    @Test
    public void testProcessCSVWithMultiplePairsAndProjects() {
        String csvContent = """
                143, 12, 2013-11-01, 2014-02-20
                144, 8, 2015-02-10, 2015-03-20
                145, 12, 2014-02-05, 2014-02-15
                145, 10, 2016-02-05, 2016-02-15
                143, 10, 2013-11-01, 2016-02-20
                143, 15, 2013-11-01, 2015-02-20
                145, 15, 2015-02-05, 2015-02-15
                140, 16, 2013-11-01, 2015-02-20
                146, 16, 2015-02-05, 2015-02-15
                147, 17, 2013-11-01, 2015-01-31
                148, 17, 2015/01/01, 2015/01/31
                150, 1, 2013-01-01, null  
                148, 21, 2015-01-01, 2015-01-31
                148, 21, 2015-01-01, 2015-01-31                           
                """;

        MockMultipartFile file = new MockMultipartFile("file.csv", csvContent.getBytes());
        String dateFormat = "YMD";
        List<Pair> result = csvService.processCSV(file, dateFormat);

        Pair expectedPair1 = new Pair(147, 148, 30);
        expectedPair1.getProjects().put(17, 30);
        Pair expectedPair2 = new Pair(145, 143, 30);
        expectedPair2.getProjects().put(10, 10);
        expectedPair2.getProjects().put(12, 10);
        expectedPair2.getProjects().put(15, 10);

        List<Pair> expectedPairs = new ArrayList<>();

        expectedPairs.add(expectedPair1);
        expectedPairs.add(expectedPair2);

        Assertions.assertEquals(2, result.size());

        for (int i = 0; i < result.size(); i++) {
            Assertions.assertEquals(expectedPairs.get(i).getEmployeeOneId(), result.get(i).getEmployeeOneId());
            Assertions.assertEquals(expectedPairs.get(i).getEmployeeTwoId(), result.get(i).getEmployeeTwoId());
            Assertions.assertEquals(expectedPairs.get(i).getDaysWorkedTogether(), result.get(i).getDaysWorkedTogether());

            for (Integer projectId : result.get(i).getProjects().keySet()) {
                Assertions.assertTrue(result.get(i).getProjects().containsKey(projectId));
                Assertions.assertEquals(expectedPairs.get(i).getProjects().get(projectId), (int) result.get(i).getProjects().get(projectId));
            }
        }
    }


    @Test
    public void testProcessCSVInvalidData() {

        String csvContent = """
                143, 12, 2013-11-01, 2014-02-20
                144, 12,
                145, 12, 2014-02-05, 2014-02-15""";

        MockMultipartFile file = new MockMultipartFile("file.csv", csvContent.getBytes());
        String dateFormat = "YMD";
        Assertions.assertThrows(IllegalArgumentException.class, () -> csvService.processCSV(file, dateFormat));
    }
}
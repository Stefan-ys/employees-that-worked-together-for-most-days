package com.example.employees;

import com.example.employees.configuration.DateParser;
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

        List<Pair> result = csvService.processCSV(file);

        Assertions.assertEquals(1, result.size());

        Pair expectedPair = new Pair(143, 145, 10);
        expectedPair.getProjects().put(12, 10);
        Assertions.assertEquals(1, result.size());

        Assertions.assertEquals(result.get(0).getEmployee1Id(), expectedPair.getEmployee1Id());
        Assertions.assertEquals(result.get(0).getEmployee2Id(), expectedPair.getEmployee2Id());
        Assertions.assertEquals(result.get(0).getDaysWorkedTogether(), (expectedPair.getDaysWorkedTogether()));

        Assertions.assertTrue(result.get(0).getProjects().containsKey(12));
        Assertions.assertEquals((int) result.get(0).getProjects().get(12), expectedPair.getProjects().get(12));
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
                148, 17, 2015-01-01, 2015-01-31
                150, 1, 2013-01-01, null   
                148, 21, 2015-01-01, 2015-01-31
                148, 21, 2015-01-01, 2015-01-31                           
                """;


        MockMultipartFile file = new MockMultipartFile("file.csv", csvContent.getBytes());

        List<Pair> result = csvService.processCSV(file);


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
            Assertions.assertEquals(result.get(i).getEmployee1Id(), expectedPairs.get(i).getEmployee1Id());
            Assertions.assertEquals(result.get(i).getEmployee2Id(), expectedPairs.get(i).getEmployee2Id());
            Assertions.assertEquals(result.get(i).getDaysWorkedTogether(), expectedPairs.get(i).getDaysWorkedTogether());

            for (Integer projectId : result.get(i).getProjects().keySet()) {
                Assertions.assertTrue(result.get(i).getProjects().containsKey(projectId));
                Assertions.assertEquals((int) result.get(i).getProjects().get(projectId), expectedPairs.get(i).getProjects().get(projectId));
            }
        }
    }
}

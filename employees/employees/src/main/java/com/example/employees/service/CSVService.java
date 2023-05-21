package com.example.employees.service;

import com.example.employees.configuration.DateParser;
import com.example.employees.entities.Employee;
import com.example.employees.entities.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CSVService {
    private final DateParser dateParser;

    public CSVService(DateParser dateParser) {
        this.dateParser = dateParser;
    }

    public List<Pair> processCSV(MultipartFile file) {

        Map<Integer, List<Employee>> projects = new HashMap<>();


        // This try/catch block do the iteration through input file content and store employee data by project id.
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] arr = line.split(",[\\s+]?");

                // In case input is in one column
                if (arr.length == 1) {
                    arr = arr[0].split(",[\\s+]?");
                }


                int employeeId = Integer.parseInt(arr[0].trim());
                int projectId = Integer.parseInt(arr[1].trim());
                LocalDate dateStart = dateParser.parseDate(arr[2].trim());
                LocalDate dateEnd;
                if (arr[3].equalsIgnoreCase("NULL")) {
                    dateEnd = LocalDate.now();
                } else {
                    dateEnd = dateParser.parseDate(arr[3]);
                }

                Employee employee = new Employee(employeeId, dateStart, dateEnd);

                if (!projects.containsKey(projectId)) {
                    projects.put(projectId, new ArrayList<>());
                }
                projects.get(projectId).add(employee);

            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Error processing CSV file: " + e.getMessage());
        }
        // This list holds the result of the pair that worked together for most days,
        // data will be stored in list in case there is more than one pair with the same result.
        List<Pair> result = new ArrayList<>();
        int mostDays = 0;

        // This map holds the date of all the pairs that have worked together.
        Map<Integer, Map<Integer, Pair>> employeesPairs = new HashMap<>();

        // This iteration do  the combinations of pairs in a project and collect them to employeesPairs map.
        for (Integer projectId : projects.keySet()) {
            List<Employee> employees = projects.get(projectId);
            for (int i = 0; i < employees.size(); i++) {
                Employee employeeOne = employees.get(i);
                for (int j = i + 1; j < employees.size(); j++) {
                    Employee employeeTwo = employees.get(j);

                    // Collect the most recent date form project start date from the two employees.
                    LocalDate dateStarted = employeeOne.getDateStarted()
                            .isAfter(employeeTwo.getDateStarted()) ?
                            employeeOne.getDateStarted() :
                            employeeTwo.getDateStarted();

                    // Collect the oldest date form project end date from the two employees.
                    LocalDate dateEnded = employeeOne.getDateEnded()
                            .isBefore(employeeTwo.getDateEnded()) ?
                            employeeOne.getDateEnded() :
                            employeeTwo.getDateEnded();

                    if (dateStarted.isBefore(dateEnded)) {
                        int days = (int) ChronoUnit.DAYS.between(dateStarted, dateEnded);
                        if (employeesPairs.containsKey(employeeOne.getId())) {
                            mostDays = initializePairAndGetMostDays(result, mostDays, employeesPairs, projectId, employeeOne, employeeTwo, days);
                        } else if (employeesPairs.containsKey(employeeTwo.getId())) {
                            mostDays = initializePairAndGetMostDays(result, mostDays, employeesPairs, projectId, employeeTwo, employeeOne, days);
                        } else {
                            Pair newPair = new Pair(employeeOne.getId(), employeeTwo.getId(), days);
                            newPair.getProjects().put(projectId, days);
                            Map<Integer, Pair> newPairMap = new HashMap<>();
                            newPairMap.put(employeeTwo.getId(), newPair);
                            employeesPairs.put(employeeOne.getId(), newPairMap);
                            mostDays = checkDays(days, mostDays, result, newPair);
                        }
                    }
                }
            }

        }


        return result;
    }

    private int initializePairAndGetMostDays(List<Pair> result, int mostDays, Map<Integer, Map<Integer, Pair>> employeesPairs, Integer projectId, Employee employeeOne, Employee employeeTwo, int days) {
        if (!employeesPairs.get(employeeOne.getId()).containsKey(employeeTwo.getId())) {
            Pair newPair = new Pair(employeeOne.getId(), employeeTwo.getId(), days);
            newPair.getProjects().put(projectId, days);
            employeesPairs.get(employeeOne.getId()).put(employeeTwo.getId(), newPair);
            mostDays = checkDays(newPair.getDaysWorkedTogether(), mostDays, result, newPair);
        } else {
            Pair pair = employeesPairs.get(employeeOne.getId()).get(employeeTwo.getId());
            pair.setDaysWorkedTogether(pair.getDaysWorkedTogether() + days);
            pair.getProjects().putIfAbsent(projectId, 0);
            pair.getProjects().put(projectId, pair.getProjects().get(projectId) + days);
            mostDays = checkDays(pair.getDaysWorkedTogether(), mostDays, result, pair);
        }
        return mostDays;
    }

    private int checkDays(int days, int mostDays, List<Pair> pairs, Pair pair) {
        if (days < mostDays) {
            return mostDays;
        }
        if (days > mostDays) {
            pairs.clear();
        }
        pairs.add(pair);
        return days;
    }
}

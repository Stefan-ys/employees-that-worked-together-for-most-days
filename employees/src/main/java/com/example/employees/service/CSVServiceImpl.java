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
import java.util.*;

@Service
public class CSVServiceImpl implements CSVService {
    private final DateParser dateParser;

    public CSVServiceImpl(DateParser dateParser) {
        this.dateParser = dateParser;
    }

    @Override
    public List<Pair> processCSV(MultipartFile file) {

        Map<Integer, List<Employee>> projects = getProjectFromFile(file);

        return getPairThatWorkedTogetherMostDays(projects);

    }

    private Map<Integer, List<Employee>> getProjectFromFile(MultipartFile file) {
        // This map hold the list of employees by project, key value is the project id.
        Map<Integer, List<Employee>> projects = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] arr = line.split(",[\\s+]?");
                System.out.println(line);
                if (arr.length < 3) {
                    throw new IllegalArgumentException("Missing data from the input");
                }

                int employeeId = Integer.parseInt(arr[0].trim());
                int projectId = Integer.parseInt(arr[1].trim());
                LocalDate dateStart = dateParser.parseDate(arr[2].trim());
                LocalDate dateEnd;

                if (arr.length == 3 || arr[3].equalsIgnoreCase("NULL")) {
                    dateEnd = LocalDate.now();
                } else {
                    dateEnd = dateParser.parseDate(arr[3]);
                }

                Employee employee = new Employee(employeeId, dateStart, dateEnd);

                projects.putIfAbsent(projectId, new ArrayList<>());
                projects.get(projectId).add(employee);

            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error processing CSV file: " + e.getMessage());
        }
        return projects;
    }

    private List<Pair> getPairThatWorkedTogetherMostDays(Map<Integer, List<Employee>> projects) {
        // This list holds the result of the pair that worked together for most days,
        // data will be stored in list in case there is more than one pair with the same result.
        List<Pair> result = new ArrayList<>();
        int mostDays = 0;

        // This nested map holds the data of all the pairs that have worked together,
        // key value in the first map is the first employee id
        // and the key value of the second map is the second employee id.
        Map<Integer, Map<Integer, Pair>> employeesPairs = new HashMap<>();

        // This iteration do the combinations of pairs in a project and collect them to employeesPairs map.
        for (Integer projectId : projects.keySet()) {
            List<Employee> employees = projects.get(projectId);
            for (int i = 0; i < employees.size(); i++) {
                Employee employeeOne = employees.get(i);
                for (int j = i + 1; j < employees.size(); j++) {
                    Employee employeeTwo = employees.get(j);

                    if (employeeOne.getId() == employeeTwo.getId()) {
                        continue;
                    }
                    // Collect the most recent date form project start date from the two employees.
                    LocalDate dateStarted = employeeOne.getDateStarted().isAfter(employeeTwo.getDateStarted()) ? employeeOne.getDateStarted() : employeeTwo.getDateStarted();

                    // Collect the oldest date form project end date from the two employees.
                    LocalDate dateEnded = employeeOne.getDateEnded().isBefore(employeeTwo.getDateEnded()) ? employeeOne.getDateEnded() : employeeTwo.getDateEnded();


                    if (dateStarted.isBefore(dateEnded)) {
                        int days = (int) ChronoUnit.DAYS.between(dateStarted, dateEnded);

                        // Check if the pair already exists in employeePairs, if yes then add project to the pair and if not create a new pair
                        if ((employeesPairs.containsKey(employeeOne.getId()) && employeesPairs.get(employeeOne.getId()).containsKey(employeeTwo.getId()))) {
                            mostDays = addProjectToPairAndGetMostDays(result, mostDays, employeesPairs, projectId, employeeOne, employeeTwo, days);
                        } else if (employeesPairs.containsKey(employeeTwo.getId()) && employeesPairs.get(employeeTwo.getId()).containsKey(employeeOne.getId())) {
                            mostDays = addProjectToPairAndGetMostDays(result, mostDays, employeesPairs, projectId, employeeTwo, employeeOne, days);
                        } else {
                            mostDays = initializePairAndGetMostDays(result, mostDays, employeesPairs, projectId, employeeOne, employeeTwo, days);
                        }
                    }
                }
            }
        }
        return result;
    }

    private int addProjectToPairAndGetMostDays(List<Pair> result, int mostDays, Map<Integer, Map<Integer, Pair>> employeesPairs, int projectId, Employee employeeOne, Employee employeeTwo, int days) {

        Pair pair = employeesPairs.get(employeeOne.getId()).get(employeeTwo.getId());
        pair.setDaysWorkedTogether(pair.getDaysWorkedTogether() + days);
        pair.getProjects().putIfAbsent(projectId, 0);
        pair.getProjects().put(projectId, pair.getProjects().get(projectId) + days);

        return checkDays(pair.getDaysWorkedTogether(), mostDays, result, pair);

    }

    private int initializePairAndGetMostDays(List<Pair> result, int mostDays, Map<Integer, Map<Integer, Pair>> employeesPairs, int projectId, Employee employeeOne, Employee employeeTwo, int days) {

        Pair newPair = new Pair(employeeOne.getId(), employeeTwo.getId(), 0);
        Map<Integer, Pair> pairMap = new HashMap<>();
        pairMap.put(employeeTwo.getId(), newPair);
        employeesPairs.putIfAbsent(employeeOne.getId(), new HashMap<>());
        employeesPairs.get(employeeOne.getId()).put(employeeTwo.getId(), newPair);

        return addProjectToPairAndGetMostDays(result, mostDays, employeesPairs, projectId, employeeOne, employeeTwo, days);

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

package com.example.employees.service;

import com.example.employees.components.DateParser;
import com.example.employees.entities.Employee;
import com.example.employees.entities.Pair;
import com.example.employees.repositories.EmployeesByProjectRepository;
import com.example.employees.repositories.EmployeesPairsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final EmployeesByProjectRepository employeesByProjectRepository;
    private final EmployeesPairsRepository employeesPairsRepository;

    @Autowired
    public CSVServiceImpl(DateParser dateParser) {
        this.dateParser = dateParser;
        this.employeesByProjectRepository = new EmployeesByProjectRepository();
        this.employeesPairsRepository = new EmployeesPairsRepository();
    }


    @Override
    public List<Pair> processCSV(MultipartFile file, String dateFormat) {
        // Read data from file and add employees in project repository
        getProjectFromFile(file, dateFormat);
        // Return pair(s) with that worked together for the most days
        return getPairThatWorkedTogetherMostDays();
    }

    private void getProjectFromFile(MultipartFile file, String dateFormat) {

        int row = 1;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] arr = line.split(",[\\s+]?");

                if (arr.length < 4) {
                    throw new IllegalArgumentException("Missing data from the input at line: " + line);
                }

                int employeeId = Integer.parseInt(arr[0].trim());
                int projectId = Integer.parseInt(arr[1].trim());
                LocalDate dateStart = dateParser.parseDate(arr[2].trim(), dateFormat);
                LocalDate dateEnd = arr[3].equalsIgnoreCase("NULL") ? LocalDate.now() : dateParser.parseDate(arr[3].trim(), dateFormat);

                Employee employee = new Employee(employeeId, dateStart, dateEnd);

                employeesByProjectRepository.addEmployeeToProject(projectId, employee);

                row++;
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Error processing CSV file at row: " + row + " -> " + e.getMessage());
        }
    }

    private List<Pair> getPairThatWorkedTogetherMostDays() {
        // This list holds the result of the pair that worked together for most days,
        // data will be stored in list in case there is more than one pair with the same result.
        List<Pair> result = new ArrayList<>();
        int mostDays = 0;

        // Iteration do the combinations of employees by pair in a project and collect them to employeesPairs repository.
        for (Integer projectId : employeesByProjectRepository.getProjectIds()) {
            List<Employee> employees = employeesByProjectRepository.getEmployeesByProjectId(projectId);

            for (int i = 0; i < employees.size(); i++) {
                Employee employeeOne = employees.get(i);

                for (int j = i + 1; j < employees.size(); j++) {
                    Employee employeeTwo = employees.get(j);

                    if (employeeOne.getId() == employeeTwo.getId()) {
                        continue;
                    }

                    LocalDate dateStarted = getMostRecentDate(employeeOne.getDateStarted(), employeeTwo.getDateStarted());

                    LocalDate dateEnded = getOldestDate(employeeOne.getDateEnded(), employeeTwo.getDateEnded());


                    if (dateStarted.isBefore(dateEnded)) {
                        int days = (int) ChronoUnit.DAYS.between(dateStarted, dateEnded);

                        Pair pair = employeesPairsRepository.getEmployeesPair(employeeOne.getId(), employeeTwo.getId());

                        pair.addProjectToPair(projectId, days);

                        mostDays = checkDays(pair.getDaysWorkedTogether(), mostDays, result, pair);
                    }
                }
            }
        }
        return result;
    }

    private LocalDate getMostRecentDate(LocalDate dateOne, LocalDate dateTwo) {
        return dateOne.isAfter(dateTwo) ? dateOne : dateTwo;
    }

    private LocalDate getOldestDate(LocalDate dateOne, LocalDate dateTwo) {
        return dateOne.isBefore(dateTwo) ? dateOne : dateTwo;
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

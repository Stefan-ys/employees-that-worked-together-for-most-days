package com.example.employees.service;

import com.example.employees.configuration.DateParser;
import com.example.employees.entity.Employee;
import com.example.employees.entity.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
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

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));


            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",\\s+");
                int employeeId = Integer.parseInt(arr[0]);
                int projectId = Integer.parseInt(arr[1]);
                LocalDate dateStart = dateParser.parseDate(arr[2]);
                LocalDate dateEnd;
                if (arr[3].equalsIgnoreCase("NULL")) {
                    dateEnd = LocalDate.now();
                } else {
                    dateEnd = dateParser.parseDate(arr[3]);
                }
                //To delete
                System.out.printf("%s - %s - %s - %s%n", employeeId, projectId, dateStart, dateEnd);

                Employee employee = new Employee(employeeId, dateStart, dateEnd);

                if (!projects.containsKey(projectId)) {
                    projects.put(projectId, new ArrayList<>());
                }
                projects.get(projectId).add(employee);


            }

        } catch (Exception e) {

        }

        return new ArrayList<>();
    }
}

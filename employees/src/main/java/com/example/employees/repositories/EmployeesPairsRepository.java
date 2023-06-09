package com.example.employees.repositories;

import com.example.employees.entities.Pair;
import java.util.HashMap;
import java.util.Map;


public class EmployeesPairsRepository {

    private final Map<String, Pair> employeesPairs;

    public EmployeesPairsRepository() {
        this.employeesPairs = new HashMap<>();}

    public void addEmployeesPair(Pair pair) {
        String keyValue = pair.getEmployeeOneId() + "@" + pair.getEmployeeTwoId();
        employeesPairs.putIfAbsent(keyValue, pair);
    }

    public Pair getEmployeesPair(int employeeOneId, int employeeTwoId) {

         String keyValue = employeeOneId + "@" + employeeTwoId;
        if (employeesPairs.containsKey(keyValue)) {
            return employeesPairs.get(keyValue);
        }
        keyValue = employeeTwoId + "@" + employeeOneId;
        if (employeesPairs.containsKey(keyValue)) {
            return employeesPairs.get(keyValue);
        }
        return createNewPair(employeeOneId, employeeTwoId);
    }

    private Pair createNewPair(int employeeOneId, int employeeTwoId) {
        Pair newPair = new Pair(employeeOneId, employeeTwoId, 0);
        addEmployeesPair(newPair);
        return newPair;
    }

}

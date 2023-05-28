package com.example.employees.repositories;

import com.example.employees.components.HashingEmployeePair;
import com.example.employees.entities.Pair;
import java.util.HashMap;
import java.util.Map;


public class EmployeesPairsRepository {

    private final Map<String, Pair> employeesPairs;
    private final HashingEmployeePair hashingEmployeePair;

    public EmployeesPairsRepository() {
        this.employeesPairs = new HashMap<>();
        this.hashingEmployeePair = new HashingEmployeePair();
    }

    public void addEmployeesPair(Pair pair) {
        String keyValue = hashingEmployeePair.hashing(pair.getEmployeeOneId(), pair.getEmployeeTwoId());
        if (!employeesPairs.containsKey(keyValue)) {
            employeesPairs.put(keyValue, pair);
        }
    }

    public Pair getEmployeesPair(int employeeOneId, int employeeTwoId) {

        String keyValue = hashingEmployeePair.hashing(employeeOneId, employeeTwoId);
        if (employeesPairs.containsKey(keyValue)) {
            return employeesPairs.get(keyValue);
        }
        keyValue = hashingEmployeePair.hashing(employeeTwoId, employeeOneId);
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

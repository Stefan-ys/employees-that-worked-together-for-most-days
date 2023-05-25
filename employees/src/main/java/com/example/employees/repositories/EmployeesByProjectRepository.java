package com.example.employees.repositories;


import com.example.employees.entities.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmployeesByProjectRepository {
    private final Map<Integer, List<Employee>> projects;

    public EmployeesByProjectRepository() {
        this.projects = new HashMap<>();
    }

    public void addEmployeeToProject(int projectId, Employee employee) {
        projects.putIfAbsent(projectId, new ArrayList<>());
        projects.get(projectId).add(employee);
    }

    public List<Integer> getProjectIds() {
        return projects.keySet().stream().toList();
    }

    public List<Employee> getEmployeesByProjectId(int projectId) {
        return projects.get(projectId);
    }
}

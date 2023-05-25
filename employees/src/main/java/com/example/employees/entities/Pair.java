package com.example.employees.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class Pair {
    @JsonProperty("employee_id_1")
    private int EmployeeOneId;

    @JsonProperty("employee_id_2")
    private int EmployeeTwoId;

    @JsonProperty("days_worked_together")
    private int daysWorkedTogether;

    // Stores projectId and days worked together by project
    @JsonProperty("projects")
    private Map<Integer, Integer> projects;

    public Pair(int employeeOneId, int employeeTwoId, int days) {
        EmployeeOneId = employeeOneId;
        EmployeeTwoId = employeeTwoId;
        this.daysWorkedTogether = days;
        this.projects = new HashMap<>();
    }

    public int getEmployeeOneId() {
        return EmployeeOneId;
    }

    public int getEmployeeTwoId() {
        return EmployeeTwoId;
    }

    public int getDaysWorkedTogether() {
        return daysWorkedTogether;
    }


    public Map<Integer, Integer> getProjects() {
        return projects;
    }

    public void addProjectToPair(int projectId, int days) {
        this.projects.putIfAbsent(projectId, 0);
        this.projects.put(projectId, projects.get(projectId) + days);
        this.daysWorkedTogether += days;
    }
}

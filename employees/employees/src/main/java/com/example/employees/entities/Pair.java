package com.example.employees.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class Pair {
    @JsonProperty("employee_id_1")
    private int Employee1Id;


    @JsonProperty("employee_id_2")
    private int Employee2Id;

    @JsonProperty("days_worked_together")
    private int daysWorkedTogether;

    @JsonProperty("projects")
    private Map<Integer, Integer> projects;

    public Pair(int employee1ID, int employee2ID, int daysWorkedTogether) {
        Employee1Id = employee1ID;
        Employee2Id = employee2ID;
        this.daysWorkedTogether = daysWorkedTogether;
        this.projects = new HashMap<>();
    }

    public int getEmployee1Id() {
        return Employee1Id;
    }

    public Pair setEmployee1Id(int employee1Id) {
        Employee1Id = employee1Id;
        return this;
    }

    public int getEmployee2Id() {
        return Employee2Id;
    }

    public Pair setEmployee2Id(int employee2Id) {
        Employee2Id = employee2Id;
        return this;
    }

    public int getDaysWorkedTogether() {
        return daysWorkedTogether;
    }

    public Pair setDaysWorkedTogether(int daysWorkedTogether) {
        this.daysWorkedTogether = daysWorkedTogether;
        return this;
    }

    public Map<Integer, Integer> getProjects() {
        return projects;
    }

    public Pair setProjects(Map<Integer, Integer> projects) {
        this.projects = projects;
        return this;
    }

}
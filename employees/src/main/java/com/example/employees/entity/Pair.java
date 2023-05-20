package com.example.employees.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashSet;
import java.util.Set;

public class Pair {
    @JsonProperty("employee_id_1")
    private int Employee1Id;


    @JsonProperty("employee_id_2")
    private int Employee2Id;

    @JsonProperty("days_worked_together")
    private int daysWorkedTogether;

    private Set<Integer> projects;

    public Pair(int employee1ID, int employee2ID, int daysWorkedTogether) {
        Employee1Id = employee1ID;
        Employee2Id = employee2ID;
        this.daysWorkedTogether = daysWorkedTogether;
        this.projects = new HashSet<>();
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

    public Set<Integer> getProjects() {
        return projects;
    }

    public Pair setProjects(Set<Integer> projects) {
        this.projects = projects;
        return this;
    }

    @JsonProperty("projects")
    public String getProjectsAsString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Integer integer : projects) {
            stringBuilder.append(integer).append(", ");
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        return stringBuilder.toString();
    }
}

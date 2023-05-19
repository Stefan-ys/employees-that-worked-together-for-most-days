package com.example.employees.entity;

import java.time.LocalDate;

public class Employee {
    private int id;

    private LocalDate dateStarted;

    private LocalDate dateEnded;



    public Employee(int id, LocalDate dateStarted, LocalDate dateEnded) {
        this.id = id;
        this.dateStarted = dateStarted;
        this.dateEnded = dateEnded;
    }
}

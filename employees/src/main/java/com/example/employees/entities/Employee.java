package com.example.employees.entities;

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

    public int getId() {
        return id;
    }

    public LocalDate getDateStarted() {
        return dateStarted;
    }

    public LocalDate getDateEnded() {
        return dateEnded;
    }
}

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

    public Employee setId(int id) {
        this.id = id;
        return this;
    }

    public LocalDate getDateStarted() {
        return dateStarted;
    }

    public Employee setDateStarted(LocalDate dateStarted) {
        this.dateStarted = dateStarted;
        return this;
    }

    public LocalDate getDateEnded() {
        return dateEnded;
    }

    public Employee setDateEnded(LocalDate dateEnded) {
        this.dateEnded = dateEnded;
        return this;
    }
}

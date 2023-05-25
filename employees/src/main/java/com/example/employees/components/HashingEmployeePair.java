package com.example.employees.components;

import org.springframework.stereotype.Component;

@Component
public class HashingEmployeePair {

    public String hashing(int employeeOneId, int employeeTwoId) {
        int digitsCountEmployeeOneId = getCountOfDigitsInNumber(employeeOneId);
        int digitsCountEmployeeTwoId = getCountOfDigitsInNumber(employeeTwoId);

        return String.format("%s%s%s%s", digitsCountEmployeeOneId, digitsCountEmployeeTwoId, employeeOneId, employeeTwoId).intern();
    }

    private int getCountOfDigitsInNumber(int num) {
        int count = 0;

        while (num != 0) {
            num /= 10;
            ++count;
        }
        return count % 100;
    }

}

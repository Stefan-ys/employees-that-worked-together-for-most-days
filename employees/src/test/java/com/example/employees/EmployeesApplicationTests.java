package com.example.employees;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeesApplicationTests {

    @Test
    void contextLoads() {
        EmployeesApplication.main(new String[]{});

        Assertions.assertTrue(true);
    }
}

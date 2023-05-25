package com.example.employees;


import com.example.employees.components.HashingEmployeePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HashingEmployeePairTest {
    @InjectMocks
    private HashingEmployeePair hashingEmployeePair;

    @Test
    public void testHashing() {
        int employeeOneId = 12;
        int employeeTwoId = 6;
        String expectedHash = "21126";

        String actualHash = hashingEmployeePair.hashing(employeeOneId, employeeTwoId);

        Assertions.assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testHashingWithLargeIds() {
        int employeeOneId = 123456789;
        int employeeTwoId = 987654321;
        String expectedHash = "99123456789987654321";

        String actualHash = hashingEmployeePair.hashing(employeeOneId, employeeTwoId);

        Assertions.assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testHashingWithRepeatingNums() {
        int employeeOneId = 111;
        int employeeTwoId = 11;
        String expectedHashOne = "3211111";

        String actualHashOne = hashingEmployeePair.hashing(employeeOneId, employeeTwoId);


        Assertions.assertEquals(expectedHashOne, actualHashOne);

        int employeeThreeId = 1;
        int employeeFourId = 1111;
        String expectedHashTwo = "1411111";

        String actualHashTwo = hashingEmployeePair.hashing(employeeThreeId, employeeFourId);

        Assertions.assertEquals(expectedHashTwo, actualHashTwo);
    }


}

package com.karthik.calendar.repo;

import com.karthik.calendar.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepoTest {

    @Autowired
    private EmployeeRepo employeeRepo;

    @BeforeEach
    void setUp() {
        employeeRepo.deleteAll(); // Clear database before each test
    }

    @Test
    void testSaveAndFindEmployee() {
        Employee employee = new Employee();
        employee.setEmployeeName("John Doe");

        employeeRepo.save(employee);
        assertThat(employeeRepo.findById(employee.getEmployeeId())).isPresent();
    }
}

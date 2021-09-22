package com.mastery.java.task.dao;

import com.mastery.java.task.dao.interfeces.EmployeeDao;
import com.mastery.java.task.entity.Department;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.entity.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JdbcEmployeeDao.class)
class EmployeeDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EmployeeDao employeeDao;

    private Employee testEmployee;

    @BeforeEach
    void setup() {
        testEmployee = new Employee();
        testEmployee.setFirstName("forTest");
        testEmployee.setLastName("forTest");
        testEmployee.setDateOfBirth(LocalDate.now());
        testEmployee.setGender(Gender.FEMALE);
        testEmployee.setDepartment(new Department(1L, "programmers"));
        testEmployee.setJobTitle("forTest");
    }

    @Test
    void testSave() {
        assertNotNull(employeeDao.save(testEmployee).getId());
    }

    @Test
    void testUpdate() {
        testEmployee.setId(employeeDao.save(testEmployee).getId());
        testEmployee.setFirstName("updTest");
        testEmployee.setLastName("updTest");
        testEmployee.setJobTitle("updTest");
        testEmployee.setDepartment(new Department(2L, null));
        Employee updatedEmployee = employeeDao.update(testEmployee);

        assertEquals(testEmployee, updatedEmployee);
    }

    @Test
    void testDelete() {
        Employee employeeForDelete = employeeDao.save(testEmployee);
        employeeDao.delete(employeeForDelete.getId());
        assertFalse(employeeDao.findById(employeeForDelete.getId()).isPresent());
    }

    @Test
    void testFindAll() {
        employeeDao.save(testEmployee);
        assertNotNull(employeeDao.findAll());
    }

    @Test
    void testFindById() {
        assertNotNull(employeeDao.findById(5L).get());
    }

}

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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(employeeDao.save(testEmployee) > 0);
    }

    @Test
    void testUpdate() {
        testEmployee.setId(3L);
        assertTrue(employeeDao.update(testEmployee) > 0);
    }

    @Test
    void testDelete() {
        assertTrue(employeeDao.delete(5L) > 0);
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

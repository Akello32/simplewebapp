package com.mastery.java.task.service;

import com.mastery.java.task.dao.JdbcDepartmentDao;
import com.mastery.java.task.dao.JdbcEmployeeDao;
import com.mastery.java.task.dto.EmployeeDTO;
import com.mastery.java.task.entity.Department;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.entity.enums.Gender;
import com.mastery.java.task.exception.DepartmentNotFouondException;
import com.mastery.java.task.exception.EmployeeNotFouondException;
import com.mastery.java.task.facade.EmployeeFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JdbcEmployeeDao.class, JdbcDepartmentDao.class, EmployeeService.class, EmployeeFacade.class})
public class EmployeeServiceTest {
    @Autowired
    private JdbcEmployeeDao employeeDao;
    @Autowired
    private JdbcDepartmentDao departmentDao;
    @Autowired
    private EmployeeFacade employeeFacade;

    @Autowired
    private EmployeeService employeeService;

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
        assertTrue(employeeService.save(testEmployee) > 0);
    }

    @Test
    void testSaveWithWrongDepartment() {
        testEmployee.setDepartment(new Department(-1L, "prog"));
        assertThrows(DepartmentNotFouondException.class, () -> {
            employeeService.save(testEmployee);
        });
    }

    @Test
    void testUpdate() {
        testEmployee.setId(5L);
        EmployeeDTO employeeDTO = employeeFacade.employeeToEmployeeDTO(testEmployee);
        assertTrue(employeeService.update(employeeDTO) > 0);
    }

    @Test
    void testDelete() {
        assertTrue(employeeService.delete(5L) > 0);
    }

    @Test
    void testDeleteWithWrongId() {
        assertThrows(EmployeeNotFouondException.class, () -> employeeService.delete(-1L));
    }

    @Test
    void testFindAll() {
        employeeService.save(testEmployee);
        assertNotNull(employeeService.getAllEmployees());
    }

    @Test
    void testFindById() {
        assertNotNull(employeeService.getEmployeeById(5L));
    }
}
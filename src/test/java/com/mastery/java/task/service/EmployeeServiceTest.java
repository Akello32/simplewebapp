package com.mastery.java.task.service;

import com.mastery.java.task.dao.JdbcDepartmentDao;
import com.mastery.java.task.dao.JdbcEmployeeDao;
import com.mastery.java.task.dto.EmployeeDTO;
import com.mastery.java.task.entity.Department;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.entity.enums.Gender;
import com.mastery.java.task.exception.DepartmentNotFoundException;
import com.mastery.java.task.converter.EmployeeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import({JdbcEmployeeDao.class, JdbcDepartmentDao.class, EmployeeService.class, EmployeeConverter.class})
public class EmployeeServiceTest {
    @MockBean
    private JdbcEmployeeDao employeeDao;
    @MockBean
    private JdbcDepartmentDao departmentDao;

    @Autowired
    private EmployeeConverter employeeConverter;

    @Autowired
    private EmployeeService employeeService;

    private Employee testEmployee;
    private Employee result;


    @BeforeEach
    void setup() {
        testEmployee = new Employee();
        testEmployee.setFirstName("forTest");
        testEmployee.setLastName("forTest");
        testEmployee.setDateOfBirth(LocalDate.now());
        testEmployee.setGender(Gender.FEMALE);
        testEmployee.setDepartment(new Department(1L, "programmers"));
        testEmployee.setJobTitle("forTest");

        result = new Employee();
        result.setId(1L);
        result.setFirstName("forTest");
        result.setLastName("forTest");
        result.setDateOfBirth(LocalDate.now());
        result.setGender(Gender.FEMALE);
        result.setDepartment(new Department(1L, "programmers"));
        result.setJobTitle("forTest");

        when(departmentDao.findById(1L)).thenReturn(Optional.of(new Department(1L, "programmers")));
        when(employeeDao.save(testEmployee)).thenReturn(result);
        when(employeeDao.findById(1L)).thenReturn(Optional.of(testEmployee));
    }

    @Test
    void testSave() {
        assertEquals(result, employeeService.save(testEmployee));
        verify(employeeDao).save(testEmployee);
    }

    @Test
    void testSaveWithWrongDepartment() {
        when(departmentDao.findById(-1L)).thenReturn(Optional.empty());

        testEmployee.setDepartment(new Department(-1L, "prog"));
        assertThrows(DepartmentNotFoundException.class, () -> {
            employeeService.save(testEmployee);
        });
    }

    @Test
    void testUpdate() {
        when(employeeDao.update(testEmployee)).thenReturn(testEmployee);
        when(departmentDao.findById(2L)).thenReturn(Optional.of(new Department(2L, null)));

        testEmployee.setId(employeeDao.save(testEmployee).getId());
        testEmployee.setFirstName("updTest");
        testEmployee.setLastName("updTest");
        testEmployee.setJobTitle("updTest");
        testEmployee.setDepartment(new Department(2L, null));

        EmployeeDTO employeeDTO = employeeConverter.employeeToEmployeeDTO(testEmployee);
        EmployeeDTO updatedEmployee = employeeService.update(employeeDTO);

        assertEquals(employeeDTO, updatedEmployee);
    }

    @Test
    void testDelete() {
        doNothing().when(employeeDao).delete(1L);

        employeeService.delete(1L);

        verify(employeeDao, times(1)).delete(1L);
    }

    @Test
    void testFindAll() {
        employeeService.save(testEmployee);
        assertNotNull(employeeService.getAllEmployees());
    }

    @Test
    void testFindById() {
        employeeService.save(testEmployee);
        assertNotNull(employeeService.getEmployeeById(1L));
    }
}
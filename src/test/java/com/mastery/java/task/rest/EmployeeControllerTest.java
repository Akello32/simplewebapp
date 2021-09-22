package com.mastery.java.task.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastery.java.task.dto.EmployeeDTO;
import com.mastery.java.task.entity.Department;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.entity.enums.Gender;
import com.mastery.java.task.facade.EmployeeFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(EmployeeFacade.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeFacade employeeFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeController employeeController;

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
    void testCreate() throws Exception {
        when(employeeController.createEmployee(testEmployee)).thenReturn(new ResponseEntity<>(testEmployee, HttpStatus.CREATED));
        testEmployee.setId(1L);
        this.mockMvc.perform(post("/employee/create")
                .content(objectMapper.writeValueAsBytes(testEmployee))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(testEmployee)));
    }

    @Test
    void testUpdate() throws Exception {
        testEmployee.setId(1L);
        EmployeeDTO employeeDTO = employeeFacade.employeeToEmployeeDTO(testEmployee);

        when(employeeController.updateEmployee(employeeDTO)).thenReturn(new ResponseEntity<>(testEmployee, HttpStatus.OK));

        this.mockMvc.perform(put("/employee/update")
                .content(objectMapper.writeValueAsBytes(employeeDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testEmployee)));
    }

    @Test
    void testDelete() throws Exception {
        when(employeeController.deleteEmployee("1")).thenReturn(new ResponseEntity<>("Employee was deleted", HttpStatus.OK));
        this.mockMvc.perform(delete("/employee/{employeeId}/delete", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee was deleted"));
    }

    @Test
    void testGetEmployeeProfile() throws Exception {
        when(employeeController.getEmployeeProfile("1")).thenReturn(
                new ResponseEntity<>(employeeFacade.employeeToEmployeeDTO(testEmployee), HttpStatus.OK));

        this.mockMvc.perform(get("/employee/{employeeId}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(employeeFacade.employeeToEmployeeDTO(testEmployee))));
    }

    @Test
    void testGetALlEmployees() throws Exception {
        EmployeeDTO employeeDTO = employeeFacade.employeeToEmployeeDTO(testEmployee);

        when(employeeController.getALlEmployees()).thenReturn(
                new ResponseEntity<>(new ArrayList<>(List.of(employeeDTO)), HttpStatus.OK)
        );

        this.mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ArrayList<>(List.of(employeeDTO)))));

    }
}
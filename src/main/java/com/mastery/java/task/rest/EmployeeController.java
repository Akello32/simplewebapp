package com.mastery.java.task.rest;

import com.google.common.base.Preconditions;
import com.mastery.java.task.dto.EmployeeDTO;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.facade.EmployeeFacade;
import com.mastery.java.task.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("employee")
@CrossOrigin
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeFacade employeeFacade;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeFacade employeeFacade) {
        this.employeeService = employeeService;
        this.employeeFacade = employeeFacade;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getALlEmployees() {
        List<EmployeeDTO> employeeDTOList = employeeService.getAllEmployees()
                .stream()
                .map(employeeFacade::employeeToEmployeeDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(employeeDTOList, HttpStatus.OK);
    }


    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeProfile(@PathVariable String employeeId) {
        Employee employee = employeeService.getEmployeeById(Long.parseLong(employeeId));
        EmployeeDTO employeeDTO = employeeFacade.employeeToEmployeeDTO(employee);

        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createEmployee(@RequestBody Employee employee) {
        Preconditions.checkNotNull(employee);
        Preconditions.checkArgument(employee.getDepartment() != null);
        return new ResponseEntity<>(employeeService.save(employee), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Preconditions.checkNotNull(employeeDTO);
        return new ResponseEntity<>(employeeService.update(employeeDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}/delete")
    public ResponseEntity<Object> deleteEmployee(@PathVariable String employeeId) {
        employeeService.delete(Long.parseLong(employeeId));
        return new ResponseEntity<>("Employee was deleted", HttpStatus.OK);
    }
}

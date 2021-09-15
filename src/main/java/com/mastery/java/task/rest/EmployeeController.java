package com.mastery.java.task.rest;

import com.mastery.java.task.dto.EmployeeDTO;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.facade.EmployeeFacade;
import com.mastery.java.task.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("employee")
@CrossOrigin
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeFacade employeeFacade;

    @PostMapping("/create")
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getModel(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(employeeService.save(employee), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getModel(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(employeeService.update(employeeDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}/delete")
    public ResponseEntity<String> deleteEmployee(@PathVariable String employeeId) {
        employeeService.delete(Long.parseLong(employeeId));
        return new ResponseEntity<>("Employee was deleted", HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeProfile(@PathVariable String employeeId) {
        Employee employee = employeeService.getEmployeeById(Long.parseLong(employeeId));
        EmployeeDTO employeeDTO = employeeFacade.employeeToEmployeeDTO(employee);

        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDTO>> getALlEmployees() {
        List<EmployeeDTO> employeeDTOList = employeeService.getAllEmployees()
                .stream()
                .map(employeeFacade::employeeToEmployeeDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(employeeDTOList, HttpStatus.OK);
    }
}

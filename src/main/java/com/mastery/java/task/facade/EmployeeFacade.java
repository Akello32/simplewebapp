package com.mastery.java.task.facade;

import com.mastery.java.task.dto.EmployeeDTO;
import com.mastery.java.task.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFacade {
    public EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setJobTitle(employee.getJobTitle());
        employeeDTO.setDepartmentId(employee.getDepartment().getId());

        return employeeDTO;
    }
}

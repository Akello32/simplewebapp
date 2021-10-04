package com.mastery.java.task.service;

import com.mastery.java.task.dao.JdbcDepartmentDao;
import com.mastery.java.task.dao.JdbcEmployeeDao;
import com.mastery.java.task.dto.EmployeeDTO;
import com.mastery.java.task.entity.Department;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.exception.DepartmentNotFoundException;
import com.mastery.java.task.exception.EmployeeNotFoundException;
import com.mastery.java.task.converter.EmployeeConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final JdbcEmployeeDao jdbcEmployeeDao;
    private final JdbcDepartmentDao jdbcDepartmentDao;
    private final EmployeeConverter employeeConverter;


    public EmployeeService(JdbcEmployeeDao jdbcEmployeeDao, JdbcDepartmentDao jdbcDepartmentDao, EmployeeConverter employeeConverter) {
        this.jdbcEmployeeDao = jdbcEmployeeDao;
        this.jdbcDepartmentDao = jdbcDepartmentDao;
        this.employeeConverter = employeeConverter;
    }

    public Employee save(Employee employee) {
        jdbcDepartmentDao.findById(employee.getDepartment().getId())
                .orElseThrow(DepartmentNotFoundException::new);
        return jdbcEmployeeDao.save(employee);
    }

    public EmployeeDTO update(EmployeeDTO employeeDTO) {
        Employee employee = jdbcEmployeeDao.findById(employeeDTO.getId())
                .orElseThrow(EmployeeNotFoundException::new);

        if (employeeDTO.getFirstName() != null) employee.setFirstName(employeeDTO.getFirstName());
        if (employeeDTO.getLastName() != null) employee.setLastName(employeeDTO.getLastName());
        if (employeeDTO.getJobTitle() != null) employee.setJobTitle(employeeDTO.getJobTitle());

        if (employeeDTO.getDepartmentId() == null) {
            return employeeConverter.employeeToEmployeeDTO(jdbcEmployeeDao.update(employee));
        } else {
            Department department = jdbcDepartmentDao.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(DepartmentNotFoundException::new);
            employee.setDepartment(department);
        }

        return employeeConverter.employeeToEmployeeDTO(jdbcEmployeeDao.update(employee));
    }

    public void delete(Long id) {
        getEmployeeById(id);
        jdbcEmployeeDao.delete(id);
    }

    public List<EmployeeDTO> getAllEmployees() {
        return jdbcEmployeeDao.findAll()
                .stream()
                .map(employeeConverter::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(Long id) {
        return employeeConverter.employeeToEmployeeDTO(jdbcEmployeeDao.findById(id)
                .orElseThrow(EmployeeNotFoundException::new));
    }
}

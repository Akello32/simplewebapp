package com.mastery.java.task.service;

import com.mastery.java.task.dao.JdbcDepartmentDao;
import com.mastery.java.task.dao.JdbcEmployeeDao;
import com.mastery.java.task.dto.EmployeeDTO;
import com.mastery.java.task.entity.Department;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.exception.DepartmentNotFoundException;
import com.mastery.java.task.exception.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final JdbcEmployeeDao jdbcEmployeeDao;
    private final JdbcDepartmentDao jdbcDepartmentDao;

    @Autowired
    public EmployeeService(JdbcEmployeeDao jdbcEmployeeDao, JdbcDepartmentDao jdbcDepartmentDao) {
        this.jdbcEmployeeDao = jdbcEmployeeDao;
        this.jdbcDepartmentDao = jdbcDepartmentDao;
    }

    public Employee save(Employee employee) {
        jdbcDepartmentDao.findById(employee.getDepartment().getId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        return jdbcEmployeeDao.save(employee);
    }

    public Employee update(EmployeeDTO employeeDTO) {
        Employee employee = jdbcEmployeeDao.findById(employeeDTO.getId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        if (employeeDTO.getFirstName() != null) employee.setFirstName(employeeDTO.getFirstName());
        if (employeeDTO.getLastName() != null) employee.setLastName(employeeDTO.getLastName());
        if (employeeDTO.getJobTitle() != null) employee.setJobTitle(employeeDTO.getJobTitle());

        if (employeeDTO.getDepartmentId() == null) {
            return jdbcEmployeeDao.update(employee);
        } else {
            Department department = jdbcDepartmentDao.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
            employee.setDepartment(department);
        }

        return jdbcEmployeeDao.update(employee);
    }

    public void delete(Long id) {
        getEmployeeById(id);
        jdbcEmployeeDao.delete(id);
    }

    public List<Employee> getAllEmployees() {
        return jdbcEmployeeDao.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return jdbcEmployeeDao.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
    }
}

package com.mastery.java.task.service;

import com.mastery.java.task.dao.JdbcDepartmentDao;
import com.mastery.java.task.dao.JdbcEmployeeDao;
import com.mastery.java.task.dto.EmployeeDTO;
import com.mastery.java.task.entity.Department;
import com.mastery.java.task.exception.DepartmentNotFouondException;
import com.mastery.java.task.exception.EmployeeNotFouondException;
import com.mastery.java.task.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final JdbcEmployeeDao jdbcEmployeeDao;
    private final JdbcDepartmentDao jdbcDepartmentDao;

    @Autowired
    public EmployeeService(JdbcEmployeeDao jdbcEmployeeDao, JdbcDepartmentDao jdbcDepartmentDao) {
        this.jdbcEmployeeDao = jdbcEmployeeDao;
        this.jdbcDepartmentDao = jdbcDepartmentDao;
    }

    public int save(Employee employee) {
        Optional<Department> department = jdbcDepartmentDao.findById(employee.getDepartment().getId());
        if (department.isPresent()) {
            return jdbcEmployeeDao.save(employee);
        } else {
            throw new DepartmentNotFouondException("Department not found");
        }
    }

    public int update(EmployeeDTO employeeDTO) {
        Employee employee = jdbcEmployeeDao.findById(employeeDTO.getId())
                .orElseThrow(() -> new EmployeeNotFouondException("Employee not found"));

        if (employeeDTO.getFirstName() != null) employee.setFirstName(employeeDTO.getFirstName());
        if (employeeDTO.getLastName() != null) employee.setLastName(employeeDTO.getLastName());
        if (employeeDTO.getJobTitle() != null) employee.setJobTitle(employeeDTO.getJobTitle());

        if (employeeDTO.getDepartmentId() == null) {
            return jdbcEmployeeDao.update(employee);
        } else {
            Optional<Department> department = jdbcDepartmentDao.findById(employeeDTO.getDepartmentId());
            if (department.isPresent()) {
                employee.setDepartment(department.get());
            } else {
                throw new DepartmentNotFouondException("Department not found");
            }
        }

        return jdbcEmployeeDao.update(employee);
    }

    public int delete(Long id) {
        Optional<Employee> employee = jdbcEmployeeDao.findById(id);
        if (employee.isPresent()) {
            return jdbcEmployeeDao.delete(id);
        } else {
            throw new EmployeeNotFouondException("Employee not found");
        }
    }

    public List<Employee> getAllEmployees() {
        return jdbcEmployeeDao.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return jdbcEmployeeDao.findById(id)
                .orElseThrow(() -> new EmployeeNotFouondException("Employee not found"));
    }

}

package com.mastery.java.task.dao.interfeces;

import com.mastery.java.task.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao {
    int save(Employee employee);

    int update(Employee employee);

    int delete(Long employeeId);

    List<Employee> findAll();

    Optional<Employee> findById(Long employeeId);
}

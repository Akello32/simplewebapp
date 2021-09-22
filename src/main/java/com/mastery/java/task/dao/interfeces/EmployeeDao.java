package com.mastery.java.task.dao.interfeces;

import com.mastery.java.task.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao {
    Employee save(Employee employee);

    Employee update(Employee employee);

    void delete(Long employeeId);

    List<Employee> findAll();

    Optional<Employee> findById(Long employeeId);
}

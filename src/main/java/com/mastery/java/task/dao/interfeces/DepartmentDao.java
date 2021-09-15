package com.mastery.java.task.dao.interfeces;

import com.mastery.java.task.entity.Department;

import java.util.Optional;

public interface DepartmentDao {
    Optional<Department> findById(Long id);
}

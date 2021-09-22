package com.mastery.java.task.dao;

import com.mastery.java.task.dao.interfeces.DepartmentDao;
import com.mastery.java.task.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcDepartmentDao implements DepartmentDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcDepartmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Department> findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "select * from department where department_id = ?",
                    (rs, rowNum) -> Optional.of(
                            new Department(rs.getLong("department_id"), rs.getString("name"))
                    ),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

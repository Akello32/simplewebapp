package com.mastery.java.task.dao;

import com.mastery.java.task.dao.interfeces.DepartmentDao;
import com.mastery.java.task.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcDepartmentDao implements DepartmentDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Department> findById(Long id) {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    "select * from department where department_id = :id",
                    new MapSqlParameterSource("id", id),
                    (rs, rowNum) -> Optional.of(
                            new Department(rs.getLong("department_id"), rs.getString("name"))
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

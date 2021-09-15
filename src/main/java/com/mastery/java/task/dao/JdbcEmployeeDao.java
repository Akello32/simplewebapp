package com.mastery.java.task.dao;

import com.mastery.java.task.dao.interfeces.EmployeeDao;
import com.mastery.java.task.entity.Department;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.entity.enums.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcEmployeeDao implements EmployeeDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcEmployeeDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Employee employee) {
        LOG.info("Saving employee...");
        return jdbcTemplate.update("insert into employee (first_name, last_name, job_title, gender, date_of_birth, department_id) values(?, ?, ?, ?, ?, ?)",
                employee.getFirstName(), employee.getLastName(), employee.getJobTitle(), employee.getGender().toString(), employee.getDateOfBirth(), employee.getDepartment().getId());
    }

    @Override
    public int update(Employee employee) {
        LOG.info("Updating employee...");
        return jdbcTemplate.update("update employee set first_name = ?, last_name = ?," +
                        " job_title = ?, gender = ?, date_of_birth = ?, department_id = ?",
                employee.getFirstName(), employee.getLastName(), employee.getJobTitle(), employee.getGender().toString(), employee.getDateOfBirth(), employee.getDepartment().getId());
    }

    @Override
    public int delete(Long employeeId) {
        LOG.info("Deleting employee...");
        return jdbcTemplate.update("delete from employee where employee_id = ?", employeeId);
    }

    @Override
    public List<Employee> findAll() {
        LOG.info("Finding all employees...");
        return jdbcTemplate.query(
                "select * from employee",
                (rs, rowNum) ->
                        buildEmployee(rs)
        );
    }

    @Override
    public Optional<Employee> findById(Long employeeId) {
        LOG.info("Finding employee by id...");
        try {
            return jdbcTemplate.queryForObject(
                    "select * from employee where employee_id = ?",
                    (rs, rowNum) ->
                            Optional.of(buildEmployee(rs)),
                    employeeId
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Employee buildEmployee(ResultSet rs) throws SQLException {
        LOG.info("Building employee...");
        return new Employee(
                rs.getLong("employee_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                new Department(rs.getLong("department_id")),
                Gender.valueOf(rs.getString("gender")),
                rs.getString("job_title"),
                rs.getDate("date_of_birth").toLocalDate()
        );
    }
}

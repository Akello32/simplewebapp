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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcEmployeeDao implements EmployeeDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcEmployeeDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcEmployeeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Employee save(Employee employee) {
        LOG.info("Saving employee...");
        SimpleJdbcInsert create = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("employee")
                .usingGeneratedKeyColumns("employee_id");

        Long id = (Long.valueOf((Integer) create.executeAndReturnKey(createParam(employee))));

        return findById(id).orElse(null);
  }

    @Override
    public Employee update(Employee employee) {
        LOG.info("Updating employee...");
        jdbcTemplate.update("update employee set first_name = ?, last_name = ?," +
                        " job_title = ?, gender = ?, date_of_birth = ?, department_id = ? where employee_id = ?",
                employee.getFirstName(), employee.getLastName(), employee.getJobTitle(), employee.getGender().toString(), employee.getDateOfBirth(), employee.getDepartment().getId(), employee.getId());

        return findById(employee.getId()).orElse(null);
    }

    @Override
    public void delete(Long employeeId) {
        LOG.info("Deleting employee...");
        jdbcTemplate.update("delete from employee where employee_id = ?", employeeId);
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

    private Map<String, Object> createParam(Employee employee) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("first_name", employee.getFirstName());
        parameters.put("last_name", employee.getLastName());
        parameters.put("job_title", employee.getJobTitle());
        parameters.put("gender", employee.getGender().toString());
        parameters.put("date_of_birth", employee.getDateOfBirth());
        parameters.put("department_id", employee.getDepartment().getId());

        return parameters;
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

package com.mastery.java.task.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class EmployeeDTO {
    private Long id;
    private Optional<String> firstName = Optional.empty();
    private Optional<String> lastName = Optional.empty();
    private Optional<Long> departmentId = Optional.empty();
    private Optional<String> jobTitle = Optional.empty();

    public Optional<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Optional.ofNullable(firstName);
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Optional.ofNullable(lastName);
    }

    public Optional<Long> getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = Optional.ofNullable(departmentId);
    }

    public Optional<String> getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = Optional.ofNullable(jobTitle);
    }
}

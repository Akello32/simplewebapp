package com.mastery.java.task.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Long departmentId;
    private String jobTitle;
}

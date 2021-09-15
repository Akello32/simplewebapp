package com.mastery.java.task.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mastery.java.task.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private Department department;
    private Gender gender;
    private String jobTitle;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
package com.mastery.java.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Department {
    private Long id;
    private String name;

    public Department(Long id) {
        this.id = id;
    }
}

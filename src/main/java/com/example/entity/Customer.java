package com.example.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("customers")
public class Customer {
    @Id
    private Long id;

    private String name;

    private String email;
} 
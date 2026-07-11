package com.ankit.department_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_departments", indexes = {
        @Index(name = "idx_department_name", columnList = "dept_name")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_department_name", columnNames = "dept_name")
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Department {

    @Id
    @Column(name = "dept_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(name = "dept_name")
    @NotBlank
    private String departmentName;
}

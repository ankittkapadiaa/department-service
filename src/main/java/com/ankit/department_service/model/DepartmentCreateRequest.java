package com.ankit.department_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DepartmentCreateRequest
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCreateRequest {
    private String departmentName;
}

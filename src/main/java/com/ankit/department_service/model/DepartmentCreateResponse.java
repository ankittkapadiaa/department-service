package com.ankit.department_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DepartmentCreateResponse
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DepartmentCreateResponse {
    private Long departmentId;
    private String departmentName;
}

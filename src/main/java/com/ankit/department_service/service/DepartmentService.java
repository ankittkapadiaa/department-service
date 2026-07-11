package com.ankit.department_service.service;

import java.util.List;
import java.util.Map;

import com.ankit.department_service.model.DepartmentCreateRequest;
import com.ankit.department_service.model.DepartmentCreateResponse;

public interface DepartmentService {

    DepartmentCreateResponse createDepartment(DepartmentCreateRequest departmentCreateRequest);

    List<DepartmentCreateResponse> getAllDepartments();

    DepartmentCreateResponse getDepartmentById(Long departmentId);

    DepartmentCreateResponse updateDepartmentById(Long departmentId, DepartmentCreateRequest departmentCreateRequest);

    Map<String, Object> deleteDepartmentById(Long departmentId);

}

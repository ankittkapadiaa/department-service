package com.ankit.department_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankit.department_service.model.DepartmentCreateRequest;
import com.ankit.department_service.model.DepartmentCreateResponse;
import com.ankit.department_service.service.DepartmentService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public DepartmentCreateResponse createDepartment(@RequestBody DepartmentCreateRequest departmentCreateRequest) {
        return departmentService.createDepartment(departmentCreateRequest);
    }

    @GetMapping
    List<DepartmentCreateResponse> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("{departmentId}")
    DepartmentCreateResponse getDepartmentById(@PathVariable Long departmentId) {
        return departmentService.getDepartmentById(departmentId);
    }

    @PutMapping("{departmentId}")
    public DepartmentCreateResponse updateDepartmentById(@PathVariable Long departmentId,
            @RequestBody DepartmentCreateRequest departmentCreateRequest) {

        return departmentService.updateDepartmentById(departmentId, departmentCreateRequest);
    }

    @DeleteMapping("{departmentId}")
    public Map<String, Object> deleteDepartmentById(@PathVariable Long departmentId) {
        return departmentService.deleteDepartmentById(departmentId);
    }
}

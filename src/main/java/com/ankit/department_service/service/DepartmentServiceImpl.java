package com.ankit.department_service.service;

import org.springframework.stereotype.Service;

import com.ankit.department_service.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
}

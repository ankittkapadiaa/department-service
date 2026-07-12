package com.ankit.department_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.ankit.department_service.entity.Department;
import com.ankit.department_service.model.DepartmentCreateRequest;
import com.ankit.department_service.model.DepartmentCreateResponse;
import com.ankit.department_service.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentCreateResponse createDepartment(DepartmentCreateRequest departmentCreateRequest) {
        Department dep = modelMapper.map(departmentCreateRequest, Department.class);
        Department saved = departmentRepository.save(dep);
        return modelMapper.map(saved, DepartmentCreateResponse.class);
    }

    @Override
    public List<DepartmentCreateResponse> getAllDepartments() {

        return departmentRepository.findAll().stream()
                .map(department -> modelMapper.map(department, DepartmentCreateResponse.class))
                .toList();
    }

    @Override
    public DepartmentCreateResponse getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
        return modelMapper.map(department, DepartmentCreateResponse.class);
    }

    @Override
    public DepartmentCreateResponse updateDepartmentById(Long departmentId,
            DepartmentCreateRequest departmentCreateRequest) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
        boolean ifDepartmentAlreadyExists = departmentRepository
                .findByDepartmentName(departmentCreateRequest.getDepartmentName()).isPresent();
        if (ifDepartmentAlreadyExists) {
            throw new RuntimeException(
                    "Department already exists with name: " + departmentCreateRequest.getDepartmentName());
        }

        department.setDepartmentName(departmentCreateRequest.getDepartmentName());
        return modelMapper.map(departmentRepository.save(department), DepartmentCreateResponse.class);
    }

    @Override
    public Map<String, Object> deleteDepartmentById(Long departmentId) {
        Map<String, Object> response = new HashMap<String, Object>();
        departmentRepository.deleteById(departmentId);
        response.put("success", true);
        return response;
    }

    @Override
    public List<DepartmentCreateResponse> getDepartmentsByIds(List<Long> ids) {
        return departmentRepository.findAllById(ids).stream()
                .map(department -> modelMapper.map(department, DepartmentCreateResponse.class))
                .toList();
    }
}

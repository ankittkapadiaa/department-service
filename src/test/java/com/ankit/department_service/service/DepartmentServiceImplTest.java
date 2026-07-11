package com.ankit.department_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import com.ankit.department_service.entity.Department;
import com.ankit.department_service.model.DepartmentCreateRequest;
import com.ankit.department_service.model.DepartmentCreateResponse;
import com.ankit.department_service.repository.DepartmentRepository;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

        @Mock
        private DepartmentRepository departmentRepository;
        private ModelMapper modelmapper = new ModelMapper();

        private DepartmentServiceImpl departmentServiceImpl;

        @BeforeEach
        void setup() {
                departmentServiceImpl = new DepartmentServiceImpl(departmentRepository, modelmapper);
        }

        @Test
        void testCreateDepartment() {
                // given
                DepartmentCreateRequest departmentCreateRequest = DepartmentCreateRequest.builder()
                                .departmentName("Biology")
                                .build();
                // when
                when(departmentRepository.save(any(Department.class)))
                                .thenReturn(Department.builder().departmentName("Biology")
                                                .departmentId(1L).build());
                DepartmentCreateResponse departmentResponse = departmentServiceImpl
                                .createDepartment(departmentCreateRequest);
                // then
                DepartmentCreateResponse expectedResponse = DepartmentCreateResponse.builder()
                                .departmentName("Biology")
                                .departmentId(1L)
                                .build();

                assertThat(departmentResponse.getDepartmentId().longValue()).isNotNull();
                assertThat(departmentResponse.getDepartmentName()).isEqualTo(expectedResponse.getDepartmentName());
                assertThat(departmentResponse.getDepartmentId()).isEqualTo(expectedResponse.getDepartmentId());
        }

        @Test
        void testDeleteDepartmentById() {
                Long deptId = 1L;
                Map<String, Object> deleteDepartmentResponse = departmentServiceImpl.deleteDepartmentById(deptId);

                verify(departmentRepository).deleteById(deptId);
                assertThat(deleteDepartmentResponse).containsKey("success");
                assertThat(deleteDepartmentResponse).containsValue(true);
        }

        @Test
        void testGetAllDepartments() {
                departmentServiceImpl.getAllDepartments();

                verify(departmentRepository).findAll();
        }

        @Test
        void testGetDepartmentByIdShouldHaveValueWhenFound() {
                Long deptId = 1L;
                Department dept = Department.builder().departmentId(deptId).departmentName("Biology").build();
                when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(dept));
                // when
                DepartmentCreateResponse departmentResponse = departmentServiceImpl.getDepartmentById(deptId);
                // then
                assertThat(departmentResponse.getDepartmentId()).isEqualTo(deptId);
                assertThat(departmentResponse.getDepartmentName()).isEqualTo(dept.getDepartmentName());

        }

        @Test
        void testGetDepartmentByIdShouldHaveRuntimeExceptionWhenNotFound() {
                Long deptId = 1L;
                when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());
                // when
                assertThatThrownBy(() -> departmentServiceImpl.getDepartmentById(deptId))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessage("Department not found with id: " + deptId);
        }

        @Test
        void testUpdateDepartmentById() {
                Long deptId = 1L;
                DepartmentCreateRequest departmentCreateRequest = DepartmentCreateRequest.builder()
                                .departmentName("Humanity")
                                .build();
                Department department = Department.builder().departmentId(deptId).departmentName("Biology").build();

                when(departmentRepository.findById(eq(deptId))).thenReturn(Optional.of(department));
                when(departmentRepository.findByDepartmentName(eq(departmentCreateRequest.getDepartmentName())))
                                .thenReturn(Optional.empty());
                when(departmentRepository.save(any(Department.class))).thenReturn(
                                Department.builder().departmentId(deptId).departmentName("Humanity").build());

                DepartmentCreateResponse updateDepartmentResponse = departmentServiceImpl.updateDepartmentById(deptId,
                                departmentCreateRequest);
                assertThat(updateDepartmentResponse.getDepartmentName())
                                .isEqualTo(departmentCreateRequest.getDepartmentName());
        }

        @Test
        void testUpdateDepartmentByIdWhenNewNameAlreadyPresent() {
                Long deptId = 1L;
                DepartmentCreateRequest departmentCreateRequest = DepartmentCreateRequest.builder()
                                .departmentName("Humanity")
                                .build();
                Department department = Department.builder().departmentId(deptId).departmentName("Biology").build();

                when(departmentRepository.findById(eq(deptId))).thenReturn(Optional.of(department));
                when(departmentRepository.findByDepartmentName(eq(departmentCreateRequest.getDepartmentName())))
                                .thenReturn(Optional.of(department));

                assertThatThrownBy(() -> departmentServiceImpl.updateDepartmentById(deptId, departmentCreateRequest))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessage("Department already exists with name: "
                                                + departmentCreateRequest.getDepartmentName());
                verify(departmentRepository, never()).save(any());
        }

        @Test
        void testUpdateDepartmentByIdWhenDepartmentNotPresent() {
                Long deptId = 1L;
                DepartmentCreateRequest departmentCreateRequest = DepartmentCreateRequest.builder()
                                .departmentName("Humanity")
                                .build();
                when(departmentRepository.findById(eq(deptId))).thenReturn(Optional.empty());

                assertThatThrownBy(() -> departmentServiceImpl.updateDepartmentById(deptId, departmentCreateRequest))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessage("Department not found with id: " + deptId);

                verify(departmentRepository, never()).save(any());
                verify(departmentRepository, never()).findByDepartmentName(anyString());
        }
}

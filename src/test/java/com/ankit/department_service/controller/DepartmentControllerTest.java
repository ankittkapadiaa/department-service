package com.ankit.department_service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.ankit.department_service.entity.Department;
import com.ankit.department_service.model.DepartmentCreateResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestRestTemplate
public class DepartmentControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    // @Autowired
    // private DepartmentRepository departmentRepository;

    private static final String API_BASE_PATH = "/api/department";

    @Container
    PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:14");

    @Test
    void canEstablishConnection() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    // @BeforeEach
    // void setup() {
    // Department dept = Department.builder().departmentName("Biology").build();
    // departmentRepository.save(dept);
    // }

    // @AfterEach
    // void teardown() {
    // System.out.println("deleting all");
    // departmentRepository.deleteAll();
    // System.out.println("deleted all");
    // }

    @Test
    void testCreateDepartment() {

        Department department = Department.builder().departmentName("Adminstration").build();

        ResponseEntity<DepartmentCreateResponse> response = testRestTemplate.exchange(
                API_BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(department),
                new ParameterizedTypeReference<DepartmentCreateResponse>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<List<DepartmentCreateResponse>> listResponse = testRestTemplate.exchange(
                API_BASE_PATH,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DepartmentCreateResponse>>() {
                });
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DepartmentCreateResponse cretaeDepartmentResponse = Objects.requireNonNull(listResponse.getBody())
                .stream()
                .filter(dr -> dr.getDepartmentName().equals(department.getDepartmentName()))
                .findFirst()
                .orElseThrow();
        assertThat(cretaeDepartmentResponse.getDepartmentId()).isNotNull();
        assertThat(cretaeDepartmentResponse.getDepartmentName()).isEqualTo(department.getDepartmentName());
    }

    @Test
    void testDeleteDepartmentById() {

        Department department = Department.builder().departmentName("Finance").build();
        ResponseEntity<DepartmentCreateResponse> response = testRestTemplate.exchange(API_BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(department),
                new ParameterizedTypeReference<DepartmentCreateResponse>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DepartmentCreateResponse responseBody = Objects.requireNonNull(response.getBody());

        ResponseEntity<Map<String, Object>> deleteResponse = testRestTemplate.exchange(
                API_BASE_PATH + "/" + responseBody.getDepartmentId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });
        @Nullable
        Map<String, Object> deleteResponseMap = Objects.requireNonNull(deleteResponse.getBody());
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResponseMap).containsKey("success");
        assertThat(deleteResponseMap).containsValue(true);

    }

    @Test
    @Disabled
    void testGetAllDepartments() {

    }

    @Test
    void testGetDepartmentById() {
        Department department = Department.builder().departmentName("HR").build();
        ResponseEntity<DepartmentCreateResponse> response = testRestTemplate.exchange(API_BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(department),
                new ParameterizedTypeReference<DepartmentCreateResponse>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DepartmentCreateResponse responseBody = Objects.requireNonNull(response.getBody());

        ResponseEntity<Map<String, Object>> getByDepartmentIdResponse = testRestTemplate.exchange(
                API_BASE_PATH + "/" + responseBody.getDepartmentId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });
        assertThat(getByDepartmentIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DepartmentCreateResponse departmentById = Objects.requireNonNull(response.getBody());
        assertThat(departmentById.getDepartmentName()).isEqualTo(responseBody.getDepartmentName());
    }

    @Test
    void testUpdateDepartmentById() {
        Department department = Department.builder().departmentName("Engineering").build();
        ResponseEntity<DepartmentCreateResponse> response = testRestTemplate.exchange(API_BASE_PATH,
                HttpMethod.POST,
                new HttpEntity<>(department),
                new ParameterizedTypeReference<DepartmentCreateResponse>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DepartmentCreateResponse responseBody = Objects.requireNonNull(response.getBody());

        ResponseEntity<DepartmentCreateResponse> putResponse = testRestTemplate.exchange(
                API_BASE_PATH + "/" + responseBody.getDepartmentId(),
                HttpMethod.PUT,
                new HttpEntity<>(Department.builder().departmentName("Legal").build()),
                new ParameterizedTypeReference<DepartmentCreateResponse>() {
                });
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DepartmentCreateResponse updateDepartmentResponse = Objects.requireNonNull(putResponse.getBody());
        assertThat(updateDepartmentResponse.getDepartmentName()).isEqualTo("Legal");
        assertThat(updateDepartmentResponse.getDepartmentId()).isEqualTo(responseBody.getDepartmentId());

    }
}

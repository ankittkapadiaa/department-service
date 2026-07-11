package com.ankit.department_service.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.ankit.department_service.entity.Department;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Container
    PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:14");

    @Test
    void canEstablishConnection() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @BeforeEach
    void setup() {
        Department dept = Department.builder().departmentName("Biology").build();
        departmentRepository.save(dept);
    }

    @AfterEach
    void tearDown() {
        System.out.println("deleting all");
        departmentRepository.deleteAll();
        System.out.println("deleted all");
    }

    @Test
    void shouldFindByDepartmentName() {
        // given
        // when
        Optional<Department> byDepartmentName = departmentRepository.findByDepartmentName("Biology");
        // then
        assertThat(byDepartmentName).isPresent();
    }

    @Test
    void shouldNotFindByDepartmentName() {
        // given
        // when
        Optional<Department> byDepartmentName = departmentRepository.findByDepartmentName("Arts");
        // then
        assertThat(byDepartmentName).isNotPresent();
    }
}

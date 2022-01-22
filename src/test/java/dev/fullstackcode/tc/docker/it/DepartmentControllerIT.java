package dev.fullstackcode.tc.docker.it;


import dev.fullstackcode.tc.docker.entity.Department;
import dev.fullstackcode.tc.docker.entity.Employee;
import dev.fullstackcode.tc.docker.entity.Gender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DepartmentControllerIT extends BaseIT {

    @Test
    @Sql({ "/import.sql" })
    public void testGetDepartmentById() {

        ResponseEntity<Department> response = testRestTemplate.getForEntity( "/department/{id}",Department.class,100);
        Department dept =  response.getBody();

        assertEquals(100,dept.getId());
        assertEquals("HR", dept.getName());

    }


}
package dev.fullstackcode.tc.docker.it;


import dev.fullstackcode.tc.docker.entity.Department;
import dev.fullstackcode.tc.docker.entity.Employee;
import dev.fullstackcode.tc.docker.entity.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmployeeControllerIT extends BaseIT {

    @Container
    private static final DockerComposeContainer environment =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
                    .withExposedService("postgres", BaseIT.POSTGRES_PORT, Wait.forListeningPort())
                    .withLocalCompose(true);

    @Test
    @Sql({ "/import.sql" })
    public void testCreateEmployee() {

        Department dept = new Department();
        dept.setId(100);

        Employee emp = new Employee();

        emp.setFirst_name("abc");
        emp.setLast_name("xyz");
        emp.setDepartment(dept);
        emp.setBirth_date(LocalDate.of(1980,11,11));
        emp.setHire_date(LocalDate.of(2020,01,01));
        emp.setGender(Gender.F);

        ResponseEntity<Employee> response = testRestTemplate.postForEntity( "/employee", emp, Employee.class);

        Employee employee =  response.getBody();

        assertNotNull(employee.getId());
        assertEquals("abc", employee.getFirst_name());

    }

    @Test
    @Sql({ "/import.sql" })
    public void testGetEmployeeById() {

        ResponseEntity<Employee> response = testRestTemplate.getForEntity( "/employee/{id}",Employee.class,100);
        Employee employee =  response.getBody();

        assertEquals(100,employee.getId());
        assertEquals("Alex", employee.getFirst_name());

    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {

        String postgresUrl = environment.getServiceHost("postgres", POSTGRES_PORT)
                + ":" +
                environment.getServicePort("postgres", POSTGRES_PORT);

        registry.add("spring.datasource.url", () -> "jdbc:postgresql://"+postgresUrl+"/eis");
        registry.add("spring.datasource.username", () ->"postgres");
        registry.add("spring.datasource.password", () ->"postgres");

    }

}
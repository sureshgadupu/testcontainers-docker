package dev.fullstackcode.tc.docker.it;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
public  class BaseIT2 {
	
	@Autowired
	protected TestRestTemplate testRestTemplate ;
	private static int POSTGRES_PORT = 5432;

	private static Map<String,String> postgresEnvMap = new HashMap<>();

	@Container
	private static final  DockerComposeContainer environment =
			new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
					.withExposedService("postgres", POSTGRES_PORT, Wait.forListeningPort())
					.withLocalCompose(true);

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
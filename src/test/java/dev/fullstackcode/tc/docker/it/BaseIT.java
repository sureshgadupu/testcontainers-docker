package dev.fullstackcode.tc.docker.it;

import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static java.time.temporal.ChronoUnit.SECONDS;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public  class BaseIT {
	
	@Autowired
	protected TestRestTemplate testRestTemplate ;
	private static int POSTGRES_PORT = 5432;

	private static Map<String,String> postgresEnvMap = new HashMap<>();

//	static final  DockerComposeContainer environment;

	@Container
	private static final  DockerComposeContainer environment =
			new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
					.withExposedService("postgres", POSTGRES_PORT, Wait.forListeningPort())
					.withLocalCompose(true)
			;
//	public static DockerComposeContainer environment =
//			new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
//					.withExposedService("postgres", POSTGRES_PORT,new HostPortWaitStrategy().withStartupTimeout(Duration.of(120, SECONDS)))
//					///.withScaledService("postgres", 1)
//					.withLocalCompose(true)
//					//.withOptions("--compatibility")
//			;

//	@Container
//	 static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE)
//			.withDatabaseName("eis");

//	docker run --name postgresql-container -p 5436:5432 -e POSTGRES_PASSWORD=secret -d postgres:13.2
//	@Container
//	public static GenericContainer environment = new GenericContainer(DockerImageName.parse("postgres:13.2"))
//		.withExposedPorts(5432,5437)
//		.waitingFor(
//				Wait.forLogMessage("..*database system is ready to accept connections.*\\n", 1)
//		);

					//.withFileFromFile("src/test/resources",new File("docker-compose.yaml")))
					;

//	public static GenericContainer gc = 	new GenericContainer(new File("src/test/resources/docker-compose.yaml"))

//	static {
//		environment =
//			new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
//					.withExposedService("postgres", POSTGRES_PORT, Wait.forListeningPort())
//				//	.withLocalCompose(true)
//					;
//
//		environment.start();
//
//				Runtime.getRuntime().addShutdownHook(new Thread() {
//			public void run() {
//				//System.out.println("Running Shutdown Hook");
//				environment.stop();
//			}
//		});


//	}




	@DynamicPropertySource
	public static void properties(DynamicPropertyRegistry registry) {

		 String postgresUrl = environment.getServiceHost("postgres", POSTGRES_PORT)
				+ ":" +
				environment.getServicePort("postgres", POSTGRES_PORT);
		System.out.println("postgresUrl ---->"+postgresUrl);
		registry.add("spring.datasource.url", () -> "jdbc:postgresql://"+postgresUrl+"/eis");
		registry.add("spring.datasource.username", () ->"postgres");
		registry.add("spring.datasource.password", () ->"postgres");

	}
}
package dev.fullstackcode.tc.docker.it;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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


//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DirtiesContext
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public  class BaseIT3 {
	
	@Autowired
	protected TestRestTemplate testRestTemplate ;
	final static int POSTGRES_PORT = 5432;

	private static Map<String,String> postgresEnvMap = new HashMap<>();

	protected   DockerComposeContainer environment;


//	private static final  DockerComposeContainer environment =
//			new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
//					.withExposedService("postgres", POSTGRES_PORT, Wait.forListeningPort())
//					.withLocalCompose(true)
//			;
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

//	@BeforeAll
//	static void beforeAll() {
//		System.out.println("in start ******************************");
//		environment.start();
//	}
//
//	@AfterAll
//	static void afterAll() {
//		System.out.println("in end ******************************");
//		environment.stop();
//	}
//	@BeforeAll
//	public static beforeAll() {
//		dockerCompose.start()
//	}
//
//	@AfterAll
//	fun afterAll() {
//		dockerCompose.stop()
//	}

	@DynamicPropertySource
	public static void properties(DynamicPropertyRegistry registry) {

		 String postgresUrl = PostgresContainerConfiguration.environment.getServiceHost("postgres", POSTGRES_PORT)
				+ ":" +
				 PostgresContainerConfiguration.environment.getServicePort("postgres", POSTGRES_PORT);
		System.out.println("postgresUrl ---->"+postgresUrl);
		registry.add("spring.datasource.url", () -> "jdbc:postgresql://"+postgresUrl+"/eis");
		registry.add("spring.datasource.username", () ->"postgres");
		registry.add("spring.datasource.password", () ->"postgres");

	}
}
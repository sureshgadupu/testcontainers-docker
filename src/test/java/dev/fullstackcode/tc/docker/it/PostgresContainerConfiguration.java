package dev.fullstackcode.tc.docker.it;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

//@Configuration
public class PostgresContainerConfiguration {
    private static int POSTGRES_PORT = 5432;

    private static Map<String,String> postgresEnvMap = new HashMap<>();

    static final DockerComposeContainer environment = new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
            .withExposedService("postgres",POSTGRES_PORT, Wait.forListeningPort())

            .withLocalCompose(true)
            ;

    @PostConstruct
    public void start() {
        System.out.println("postgresUrl config---->"+"start");
        environment.start();
    }

    @PreDestroy
    public void stop() {
        System.out.println("postgresUrl config---->"+"end");
        environment.stop();
    }

//    @Bean(name="demoService")
//    public String getDataSource() {
//        return "This is test";
//    }

//    @DynamicPropertySource
//    public static void properties(DynamicPropertyRegistry registry) {
//
//        String postgresUrl = environment.getServiceHost("postgres", POSTGRES_PORT)
//                + ":" +
//                environment.getServicePort("postgres", POSTGRES_PORT);
//        System.out.println("postgresUrl config---->"+postgresUrl);
//        registry.add("spring.datasource.url", () -> "jdbc:postgresql://"+postgresUrl+"/eis");
//        registry.add("spring.datasource.username", () ->"postgres");
//        registry.add("spring.datasource.password", () ->"postgres");
//
//    }

   @Bean
    public DataSource getDataSource()
    {
        String postgresUrl = environment.getServiceHost("postgres", POSTGRES_PORT)
                + ":" +
                environment.getServicePort("postgres", POSTGRES_PORT);

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://"+postgresUrl+"/eis");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("postgres");
        return dataSourceBuilder.build();
    }

//    @Bean
//    @Primary
//    public MongoClient mongoClient() {
//        return new MongoClient(MONGO_CONTAINER.getContainerIpAddress(), MONGO_CONTAINER.getMappedPort(27017));
//    }

//    static class TestEnvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//
//        @Override
//        public void initialize(ConfigurableApplicationContext applicationContext) {
//
//            String postgresUrl = environment.getServiceHost("postgres", POSTGRES_PORT)
//                    + ":" +
//                    environment.getServicePort("postgres", POSTGRES_PORT);
//
//            TestPropertyValues values = TestPropertyValues.of(
//                    "spring.datasource.url=" + postgresUrl.getJdbcUrl(),
//                    "spring.datasource.password=" + postgresDB.getPassword(),
//                    "spring.datasource.username=" + postgresDB.getUsername()
//            );
//            values.applyTo(applicationContext);
//
//        }
//
//    }

}

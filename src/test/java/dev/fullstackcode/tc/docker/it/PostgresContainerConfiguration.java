package dev.fullstackcode.tc.docker.it;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PostgresContainerConfiguration {

    private static int POSTGRES_PORT = 5432;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    static final DockerComposeContainer environment = new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
            .withExposedService("postgres",POSTGRES_PORT, Wait.forListeningPort())

            .withLocalCompose(true)
            ;

    @PostConstruct
    public void start() {
        environment.start();
    }

    @PreDestroy
    public void stop() {
        environment.stop();
    }
   @Bean
    public DataSource getDataSource()
    {
        String postgresUrl = environment.getServiceHost("postgres", POSTGRES_PORT)
                + ":" +
                environment.getServicePort("postgres", POSTGRES_PORT);

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://"+postgresUrl+"/eis");
        dataSourceBuilder.username(datasourceUsername);
        dataSourceBuilder.password(datasourcePassword);
        return dataSourceBuilder.build();
    }


}

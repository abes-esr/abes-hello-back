package fr.abes.helloabes.web;

import fr.abes.helloabes.web.controller.jpa.ApplicationJPATestBase;
import fr.abes.helloabes.web.controller.mockito.ApplicationMockitoTestBase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"fr.abes.helloabes.core.dao"})
@EnableTransactionManagement
public class TestProfileConfiguration {

    @Profile({"test-jpa"})
    @Bean
    public ApplicationTestBase jpaApplicationTest() {
        return new ApplicationJPATestBase();
    }

    @Profile({"test-mockito"})
    @Bean
    public ApplicationTestBase mockitoApplicationTest() {
        return new ApplicationMockitoTestBase();
    }


}
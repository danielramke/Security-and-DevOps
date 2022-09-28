package de.lyth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableJpaRepositories("de.lyth.repositories")
@EntityScan("de.lyth.model.persistence")
@SpringBootApplication
public class SecurityAndDevOpsApplication {

    /**
     * This static method lunched the spring boot application for us.
     * @param args the program arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(SecurityAndDevOpsApplication.class, args);
    }

    /**
     * This method creates a default bcrypt password encoder.
     * @return the password encoder.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

package se.sowl.postHubingapi.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "se.sowl.postHubingdomain")
public class JpaConfig {
}

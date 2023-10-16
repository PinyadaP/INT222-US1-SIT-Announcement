package int221.SASBE;

import int221.SASBE.config.JwtTokenUtil;
import int221.SASBE.properties.JwtProperties;
import int221.SASBE.repository.CustomRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
@EnableConfigurationProperties({JwtProperties.class})

public class SasbeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SasbeApplication.class, args);

	}

}

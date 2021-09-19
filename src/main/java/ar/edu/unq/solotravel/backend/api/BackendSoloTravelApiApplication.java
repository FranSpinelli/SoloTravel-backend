package ar.edu.unq.solotravel.backend.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class BackendSoloTravelApiApplication {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/soloTravel");
		SpringApplication.run(BackendSoloTravelApiApplication.class, args);
	}

}

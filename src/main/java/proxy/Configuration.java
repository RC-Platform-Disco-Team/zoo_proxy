package proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Maxim Neverov
 */
@SpringBootApplication
@EnableWebMvc
public class Configuration {

    public static void main(String[] args) {
        SpringApplication.run(Configuration.class, args);
    }
}
package testingtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Configuration
@PropertySource(name = "myProperties", value = "values.properties")
public class Application {
    public static final void main(String [] args) {
        SpringApplication.run(Application.class, args);
    }
}

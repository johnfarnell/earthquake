package au.com.auspost;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 */
@SpringBootApplication
@EnableSwagger
public class EarthquakeApplication {
    public static void main(String[] args) {
        SpringApplication.run(EarthquakeApplication.class, args);
    }
}

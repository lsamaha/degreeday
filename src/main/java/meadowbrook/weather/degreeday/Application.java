package meadowbrook.weather.degreeday;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Entry point for DegreeDays service.
 */
@SpringBootApplication
@ComponentScan({"meadowbrook.weather.external", "meadowbrook.weather.degreeday"})
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("starting application ...");
        SpringApplication.run(Application.class, args);
    }

}

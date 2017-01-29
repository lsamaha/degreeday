package meadowbrook.weather.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provides the configured WeatherService.
 */
@Component
public class WeatherServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceFactory.class);

    @Autowired
    private WeatherService weatherService;

    public WeatherService getWeatherService() {
        return weatherService;
    }

    public void setWeatherService(WeatherService ws) {
        this.weatherService = ws;
        logger.info("using " + this.weatherService);
    }
}

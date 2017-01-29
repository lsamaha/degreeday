package meadowbrook.weather.external;

import meadowbrook.weather.model.DailyAverage;
import meadowbrook.weather.model.Temperature;

import java.time.LocalDate;
import java.util.Collection;

/**
 * An external service or API for retrieving daily average temperature data.
 */
public interface WeatherService {

    DailyAverage getDailyAverage(LocalDate d,
                                 String stationId,
                                 Temperature.Units units);

    Collection<DailyAverage> getDailyAverages(LocalDate startDate,
                                              LocalDate endDate,
                                              String stationId,
                                              Temperature.Units units);

}

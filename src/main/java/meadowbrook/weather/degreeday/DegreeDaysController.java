package meadowbrook.weather.degreeday;

import meadowbrook.weather.external.WeatherServiceFactory;
import meadowbrook.weather.model.DailyAverage;
import meadowbrook.weather.model.Temperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

/**
 * REST controller for degree days calculations.
 */
@RestController
public class DegreeDaysController {

    protected static final Logger logger = LoggerFactory.getLogger(DegreeDaysController.class);
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    protected static final Double HEATING_BASE_TEMP_CELSIUS = 18d;
    protected static final Double HEATING_BASE_TEMP_FAHRENHEIT = 65d;
    protected static final Double COOLING_BASE_TEMP_CELSIUS = 20d;
    protected static final Double COOLING_BASE_TEMP_FAHRENHEIT = 70d;
    @Autowired
    protected WeatherServiceFactory weatherServiceFactory;

    @RequestMapping("/degrees")
    public DegreeDays getDegreeDays(@RequestParam(value = "baseTemp", required = false) Double baseTemp,
                                           @RequestParam(value = "units", defaultValue = "C", required = false) String unitStr,
                                           @RequestParam(value = "type", defaultValue = "H", required = false) String degreeDayTypeStr,
                                           @RequestParam(value = "startDate") String startDate,
                                           @RequestParam(value = "endDate") String endDate,
                                           @RequestParam(value = "stationId") String stationId)
            throws IllegalArgumentException {
        // a good base temp varies based on units (C/F) and degree day type (heating/cooling)
        if(baseTemp == null) {
            baseTemp = getDefaultBaseFor(getDegreeDayTypeFor(degreeDayTypeStr), getUnitsFor(unitStr));
        }
        // as basis for heating or cooling
        DegreeDays.DegreeDayType degreeDayType = getDegreeDayTypeFor(degreeDayTypeStr);
        return getDegreeDays(getDailyTemps(unitStr, startDate, endDate, stationId), baseTemp, degreeDayType);
    }

    @RequestMapping("/dailies")
    public Collection<DailyAverage> getDailyTemps(@RequestParam(value = "units") String unitsStr,
                                                  @RequestParam(value = "startDate") String startDateStr,
                                                  @RequestParam(value = "endDate") String endDateStr,
                                                  @RequestParam(value = "stationId") String stationId)
            throws IllegalArgumentException {
        logger.info("getting " + startDateStr + "-" + endDateStr + " " + unitsStr + " @" + stationId);
        // parse dates
        LocalDate startDt = LocalDate.parse(startDateStr, formatter);
        LocalDate endDt = LocalDate.parse(endDateStr, formatter);

        // get data from external weather service
        Collection<DailyAverage> readings = weatherServiceFactory.getWeatherService().getDailyAverages(
                startDt, endDt, stationId, getUnitsFor(unitsStr));

        // convert units if needed - if one then all
        if (readings.iterator().hasNext() && !(unitsStr.equals(readings.iterator().next().getUnits().toString()))) {
            readings.forEach(r -> DegreeConversion.convert(r, getUnitsFor(unitsStr)));
        }
        return readings;
    }

    public static DegreeDays getDegreeDays(Collection<DailyAverage> readings,
                                           Double baseTemp, DegreeDays.DegreeDayType degreeDayType) {
        return new DegreeDays(readings, baseTemp, degreeDayType);
    }

    /**
     * Since degree days are the number of degrees below/above a desired temp, we need a different base for C/F.
     *
     * @param units the desired units
     * @return a reasonable desired base temp for the requested units
     */
    protected static double getDefaultBaseFor(DegreeDays.DegreeDayType degreeDayType,
                                              Temperature.Units units) {
        if(degreeDayType == DegreeDays.DegreeDayType.HEATING) {
            if(units.equals(Temperature.Units.CELSIUS)) {
                return HEATING_BASE_TEMP_CELSIUS;
            } else if (units.equals(Temperature.Units.FAHRENHEIT)) {
                return HEATING_BASE_TEMP_FAHRENHEIT;
            } else {
                throw new IllegalArgumentException("no default heating base exists for " + units.toString());
            }
        } else if(degreeDayType == DegreeDays.DegreeDayType.COOLING) {
            if(units.equals(Temperature.Units.CELSIUS)) {
                return COOLING_BASE_TEMP_CELSIUS;
            } else if (units.equals(Temperature.Units.FAHRENHEIT)) {
                return COOLING_BASE_TEMP_FAHRENHEIT;
            } else {
                throw new IllegalArgumentException("no default cooling base exists for " + units.toString());
            }
        } else {
            throw new IllegalArgumentException("no default base exists for " + degreeDayType.toString());
        }
    }

    public Temperature.Units getUnitsFor(String unitsStr) throws IllegalArgumentException {
        // normalize units
        if(Arrays.asList("c", "celsius", "centigrade").contains(unitsStr.toLowerCase())) {
            return Temperature.Units.CELSIUS;
        } else if(Arrays.asList("f", "fahrenheit").contains(unitsStr.toLowerCase())) {
            return Temperature.Units.FAHRENHEIT;
        } else {
            try {
                return Temperature.Units.valueOf(unitsStr);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid units specified and no translation was found for: \'"
                        + unitsStr + "\'" + ". (Try C, F, Celsius, Fahrenheit, etc.)");
            }
        }
    }

    public DegreeDays.DegreeDayType getDegreeDayTypeFor(String degreeDayTypeStr) throws IllegalArgumentException {
        // normalize degree ay type param string
        if(Arrays.asList("h", "heating").contains(degreeDayTypeStr.toLowerCase())) {
            return DegreeDays.DegreeDayType.HEATING;
        } else if(Arrays.asList("c", "cooling").contains(degreeDayTypeStr.toLowerCase())) {
            return DegreeDays.DegreeDayType.COOLING;
        } else {
            try {
                return DegreeDays.DegreeDayType.valueOf(degreeDayTypeStr);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid degree day type specified and no translation was found: \'"
                        + degreeDayTypeStr + "\'" + ". (Try C, H, Cooling, Heating, etc.)");
            }
        }
    }

    public void setWeatherServiceFactory(WeatherServiceFactory weatherServiceFactory) {
        this.weatherServiceFactory = weatherServiceFactory;
    }

}

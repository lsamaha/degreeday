package meadowbrook.weather.degreeday;

import com.fasterxml.jackson.annotation.JsonProperty;
import meadowbrook.weather.model.DailyAverage;
import meadowbrook.weather.model.Temperature;

import java.util.Collection;

/**
 * Calculates degree days from a complete set of average temperature readings and a heating or cooling base value.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Heating_degree_day">Wikipedia Degree Days</a?>
 */
public class DegreeDays {

    private int numDays = 0;
    private final Double degreeDays; // for presentation
    private final Integer numReadings; // should match number of days
    private final Double baseTemp; // a desirable heating/cooling target
    private final Temperature.Units units; // celsius/fahrenheit
    private final DegreeDayType degreeDayType; // heating/cooling
    public DegreeDays(Collection<DailyAverage> readings, Double baseTemp, DegreeDayType degreeDayType)
            throws IllegalArgumentException {
        this.numDays = readings.size();
        this.units = getUnits(readings);
        this.baseTemp = baseTemp;
        this.numReadings = readings.size();
        this.degreeDayType = degreeDayType;
        double rawDegreeDays = DegreeDaysCalculation.calculateDegreeDays(readings, baseTemp, degreeDayType);
        this.degreeDays = Math.round(rawDegreeDays * 100) / 100d;
    }

    protected static Temperature.Units getUnits(Collection<DailyAverage> readings) {
        if (readings.size() == 0) {
            return Temperature.Units.CELSIUS;
        } else {
            if (!allUnitsMatch(readings)) {
                throw new IllegalArgumentException("All units must match.");
            }
            return readings.iterator().next().getUnits();
        }
    }

    protected static boolean allUnitsMatch(Collection<DailyAverage> readings) {
        if (readings.size() == 0) {
            return true;
        } else {
            Temperature.Units firstUnit = readings.iterator().next().getUnits();
            return readings.stream().allMatch(r -> r.getUnits().equals(firstUnit));
        }
    }

    public DegreeDayType getDegreeDayType() { return degreeDayType; }

    public Temperature.Units getUnits() { return units; }

    public Double getBaseTemp() { return baseTemp; }

    public Integer getNumDays() {
        return numDays;
    }

    public Integer getNumReadings() {
        return numReadings;
    }

    public Double getDegreeDays() {
        return degreeDays;
    }

    @JsonProperty("average")
    public Double getAverageDegreesPerDay() {
        return getNumDays() > 0 ? (Math.round(degreeDays / getNumDays() * 100)) / 100d : 0d;
    }

    public enum DegreeDayType {
        @JsonProperty("heating")
        HEATING,
        @JsonProperty("cooling")
        COOLING}
}

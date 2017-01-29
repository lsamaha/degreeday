package meadowbrook.weather.degreeday;

import meadowbrook.weather.model.DailyAverage;

import java.util.Collection;

/**
 * Calculation that determines heating or cooling needs for the provided days.
 */
public class DegreeDaysCalculation {

    /**
     * Determine heating or cooling needs for the provided days.
     * @param readings average daily temperature readings
     * @param baseTemp a desirable heating/cooling target
     * @param degreeDayType weather to measure heating or cooling needs
     * @return total number of degrees below (heating) or above (cooling) the base over the provided days
     */
    public static Double calculateDegreeDays(
            Collection<DailyAverage> readings, final Double baseTemp, DegreeDays.DegreeDayType degreeDayType) {
        if (degreeDayType == DegreeDays.DegreeDayType.HEATING) {
            return readings.stream()
                    .filter(r -> r.getDegrees() < baseTemp)
                    .mapToDouble(r -> baseTemp - r.getDegrees())
                    .sum();
        } else if (degreeDayType == DegreeDays.DegreeDayType.COOLING) {
            return readings.stream()
                    .filter(r -> r.getDegrees() > baseTemp)
                    .mapToDouble(r -> r.getDegrees() - baseTemp)
                    .sum();
        } else {
            throw new IllegalArgumentException("unsupported degree day type " + degreeDayType);
        }
    }


}

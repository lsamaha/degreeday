package meadowbrook.weather.degreeday;

import meadowbrook.weather.model.DailyAverage;
import meadowbrook.weather.model.Temperature;

import java.text.DecimalFormat;

/**
 * Converts some available degree units.
 */
public class DegreeConversion {

    private static final DecimalFormat decimalFormat = new DecimalFormat(".##");

    /**
     * Modifies the provided object to conform to the requested units.
     *
     * @param dailyAverage DailyAverage info to be modified.
     * @param units        Desired units
     * @return The provided object with the requested units and adjusted degree value.
     */
    public static DailyAverage convert(DailyAverage dailyAverage, Temperature.Units units) {
        if (dailyAverage.getUnits() == units) {
            return dailyAverage;
        } else {
            if (dailyAverage.getUnits() == Temperature.Units.CELSIUS) {
                dailyAverage.setDegrees(celciusToFahrenheit(dailyAverage.getDegrees()));
            } else if (dailyAverage.getUnits() == Temperature.Units.FAHRENHEIT) {
                dailyAverage.setDegrees(fahrenheitToCelcius(dailyAverage.getDegrees()));
            }
            dailyAverage.setUnits(units);
        }
        return dailyAverage;
    }

    /**
     * Basic numeric conversion from Celsius to Fahrenheit.
     *
     * @param degreesCelsius A numeric value in degrees Celsius.
     * @return The corresponding degree value in degrees Fahrenheit.
     */
    public static double celciusToFahrenheit(double degreesCelsius) {
        return new Double(decimalFormat.format(((degreesCelsius * 9d) / 5d) + 32));
    }

    /**
     * Basic numeric conversion from Fahrenheit to Celsius.
     *
     * @param degreesFahrenheit A numeric value in degrees Fahrenheit.
     * @return The corresponding degree value in degrees Celsius.
     */
    public static double fahrenheitToCelcius(double degreesFahrenheit) {
        return ((degreesFahrenheit - 32) * 5d) / 9d;
    }
}

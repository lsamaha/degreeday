package meadowbrook.weather.degreeday;

import meadowbrook.weather.model.DailyAverage;
import meadowbrook.weather.model.Temperature;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Test unit conversion and object modification.
 */
public class DegreeConversionTest {

    private final double precision = 1e-15d;

    @Test
    public void testFahrenheitToCelsius() {
        assertEquals("Freezing is 0/32.",
                0.0d,
                DegreeConversion.fahrenheitToCelcius(32.0d), precision);
        assertEquals("Boiling is 100/212.",
                100.0d,
                DegreeConversion.fahrenheitToCelcius(212.0d), precision);
        assertEquals("Arbitrary test with high numbers.",
                7167.944444444444d,
                DegreeConversion.fahrenheitToCelcius(12934.3d), precision);
    }

    @Test
    public void testCelsiusToFahrenheit() {
        assertEquals("Freezing is 0/32.",
                32.0d,
                DegreeConversion.celciusToFahrenheit(0.0d),
                precision);
        assertEquals("Boiling is 100/212.",
                212.0d,
                DegreeConversion.celciusToFahrenheit(100.0d),
                precision);
        assertEquals("Arbitrary test with high numbers.",
                294.55555555555554d,
                DegreeConversion.fahrenheitToCelcius(562.2d), precision);
        assertEquals("Flow test.",
                40.01d,
                DegreeConversion.celciusToFahrenheit(4.45d), precision);
    }

    @Test
    public void testPrecision() {
        double temp = 1.001d;
        DailyAverage dailyAverage = DailyAverage.create().degrees(temp);
        assertEquals("does not round", dailyAverage.getDegrees(), temp, 1e-15);
    }

    @Test
    public void testCopyPolicy() {
        Double degrees = 19.5d;
        DailyAverage dd = new DailyAverage(degrees, Temperature.Units.FAHRENHEIT, new Date());
        assertEquals("The object already satisfies the unit condition",
                dd,
                DegreeConversion.convert(dd, Temperature.Units.FAHRENHEIT));
        assertEquals("The result returns a modified object not a copy",
                dd,
                DegreeConversion.convert(dd, Temperature.Units.CELSIUS));
        assertEquals("The degree value was converted in the object.",
                DegreeConversion.fahrenheitToCelcius(degrees),
                dd.getDegrees(),
                precision);
        dd = DegreeConversion.convert(dd, Temperature.Units.FAHRENHEIT);
        assertEquals("The degree value was converted in the object.",
                degrees,
                dd.getDegrees(),
                precision);
    }
}

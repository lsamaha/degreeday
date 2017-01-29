package meadowbrook.weather.degreeday;

import meadowbrook.weather.model.DailyAverage;
import meadowbrook.weather.model.Temperature;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test degree days calculation from a collection of daily average temperature readings.
 */
public class DegreeDaysTest {

    @Test
    public void testSimpleReading() {
        double base = 18d;
        DailyAverage d1 = new DailyAverage(18d, Temperature.Units.CELSIUS, new Date());
        List<DailyAverage> readings = Arrays.asList(d1);
        assertEquals("The base and the readings are equal",
                0d,
                DegreeDaysCalculation.calculateDegreeDays(readings, base, DegreeDays.DegreeDayType.HEATING),
                1e-15d
        );
    }

    @Test
    public void testReadingUnits() {
        DailyAverage d1 = new DailyAverage(18d, Temperature.Units.CELSIUS, new Date());
        DailyAverage d2 = new DailyAverage(100d, Temperature.Units.CELSIUS, new Date());
        assertEquals(Temperature.Units.CELSIUS, DegreeDays.getUnits(Arrays.asList(d1, d2)));
        DailyAverage d3 = new DailyAverage(65d, Temperature.Units.FAHRENHEIT, new Date());
        DailyAverage d4 = new DailyAverage(212d, Temperature.Units.FAHRENHEIT, new Date());
        assertEquals(Temperature.Units.FAHRENHEIT, DegreeDays.getUnits(Arrays.asList(d3, d4)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadingMismatchedUnits() {
        DailyAverage d1 = new DailyAverage(18d, Temperature.Units.CELSIUS, new Date());
        DailyAverage d2 = new DailyAverage(65d, Temperature.Units.FAHRENHEIT, new Date());
        DegreeDays.getUnits(Arrays.asList(d1, d2));
    }

    @Test
    public void testUnitsMatch() {
        DailyAverage d1 = new DailyAverage(18d, Temperature.Units.CELSIUS, new Date());
        DailyAverage d2 = new DailyAverage(100d, Temperature.Units.CELSIUS, new Date());
        DailyAverage d3 = new DailyAverage(65d, Temperature.Units.FAHRENHEIT, new Date());
        assertTrue(DegreeDays.allUnitsMatch(new LinkedList()));
        assertTrue(DegreeDays.allUnitsMatch(Arrays.asList(d1, d2)));
        assertFalse(DegreeDays.allUnitsMatch(Arrays.asList(d1, d2, d3)));
    }
}

package meadowbrook.weather.degreeday;

import meadowbrook.weather.model.DailyAverage;
import meadowbrook.weather.model.Temperature;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Test the functions used by controller entry points.
 */
public class DegreeControllerTest {

    @Test
    public void testControllerDegreeDays() {
        double base = 18d; // anything below adds degrees to degree days
        Temperature.Units units = Temperature.Units.CELSIUS;
        DailyAverage r1 = DailyAverage.create().degrees(base).units(units).date(2017, 1, 21);
        DailyAverage r2 = DailyAverage.create().degrees(base - 1).units(units).date(2017, 1, 22);
        DailyAverage r3 = DailyAverage.create().degrees(base - 2).units(units).date(2017, 1, 23);
        DailyAverage r4 = DailyAverage.create().degrees(base + 10).units(units).date(2017, 1, 24);
        DegreeDays ddh = DegreeDaysController.getDegreeDays(
                Arrays.asList(r1, r2, r3, r4), base, DegreeDays.DegreeDayType.HEATING);
        assertEquals(3d, ddh.getDegreeDays(), 1e-15);
        base = 20d;
        DegreeDays ddc = DegreeDaysController.getDegreeDays(
                Arrays.asList(r1, r2, r3, r4), base, DegreeDays.DegreeDayType.COOLING);
        assertEquals(8d, ddc.getDegreeDays(), 1e-15);
    }

    @Test
    public void testDefaultBase() {
        assertEquals(DegreeDaysController.HEATING_BASE_TEMP_CELSIUS,
                DegreeDaysController.getDefaultBaseFor(DegreeDays.DegreeDayType.HEATING,
                        Temperature.Units.CELSIUS), 1e-15);
        assertEquals(DegreeDaysController.HEATING_BASE_TEMP_FAHRENHEIT,
                DegreeDaysController.getDefaultBaseFor(DegreeDays.DegreeDayType.HEATING,
                        Temperature.Units.FAHRENHEIT), 1e-15);
        assertEquals(DegreeDaysController.COOLING_BASE_TEMP_CELSIUS,
                DegreeDaysController.getDefaultBaseFor(DegreeDays.DegreeDayType.COOLING,
                        Temperature.Units.CELSIUS), 1e-15);
        assertEquals(DegreeDaysController.COOLING_BASE_TEMP_FAHRENHEIT,
                DegreeDaysController.getDefaultBaseFor(DegreeDays.DegreeDayType.COOLING,
                        Temperature.Units.FAHRENHEIT), 1e-15);
    }

    @Test
    public void testAcceptedUnits() {
        assertEquals(Temperature.Units.CELSIUS,
                new DegreeDaysController().getUnitsFor("C"));
        assertEquals(Temperature.Units.CELSIUS,
                new DegreeDaysController().getUnitsFor("Celsius"));
        assertEquals(Temperature.Units.CELSIUS,
                new DegreeDaysController().getUnitsFor("celsius"));
        assertEquals(Temperature.Units.CELSIUS,
                new DegreeDaysController().getUnitsFor("CELSIUS"));
        assertEquals(Temperature.Units.FAHRENHEIT,
                new DegreeDaysController().getUnitsFor("F"));
        assertEquals(Temperature.Units.FAHRENHEIT,
                new DegreeDaysController().getUnitsFor("Fahrenheit"));
        assertEquals(Temperature.Units.FAHRENHEIT,
                new DegreeDaysController().getUnitsFor("fahrenheit"));
        assertEquals(Temperature.Units.FAHRENHEIT,
                new DegreeDaysController().getUnitsFor("FAHRENHEIT"));
    }
}

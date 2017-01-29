package meadowbrook.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.DecimalFormat;

/**
 * Models a temperature reading.
 */
public abstract class Temperature {

    private Units units = Units.CELSIUS;
    private Double degrees = null;

    public Units getUnits() {
        return units;
    }

    public void setUnits(Units units) {
        this.units = units;
    }

    public Double getDegrees() { return degrees; }

    public void setDegrees(Double degrees) {
        this.degrees = degrees;
    }

    public enum Units {
        CELSIUS,
        FAHRENHEIT
    }

}

package meadowbrook.weather.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Models a day and an average temperature reading for that day with Celsius or Fahrenheit units.
 */
public class DailyAverage extends Temperature {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date = null;

    private DailyAverage() {
    }

    public DailyAverage(Double degrees, Units units, Date date) {
        setDegrees(degrees);
        setUnits(units);
        setDate(date);
    }

    public static DailyAverage create() {
        return new DailyAverage();
    }

    public final DailyAverage degrees(double d) {
        setDegrees(d);
        return this;
    }

    public final DailyAverage units(Units u) {
        setUnits(u);
        return this;
    }

    public final DailyAverage date(int year, int month, int day) {
        this.date = new GregorianCalendar(year, month - 1, day).getTime();
        return this;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}

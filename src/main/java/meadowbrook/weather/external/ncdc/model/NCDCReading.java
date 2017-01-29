package meadowbrook.weather.external.ncdc.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * NCDC JSON model.
 */
public class NCDCReading {
    private String date;
    private String datatype;
    private String station;
    private String attributes;
    private Double value;
    private static final DecimalFormat decimalFormat = new DecimalFormat(".##");

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Convert NCDC degree data (320 = 32c) into common decimal degree values.
     * @return a common degree value equivalent to the NCDC calibrated 1/10 degree value
     */
    public double getDegreesCelsius() {
        return new Double(decimalFormat.format(getValue() / 10d));
    }

    public String toString() {
        return "date:" + date + ", value:" + value + ", datatype:" + datatype + ", station:" + station;
    }
}

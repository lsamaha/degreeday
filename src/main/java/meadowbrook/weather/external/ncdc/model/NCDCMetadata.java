package meadowbrook.weather.external.ncdc.model;

/**
 * NCDC JSON model.
 */
public class NCDCMetadata {
    private NCDCResultSet resultset;

    public NCDCResultSet getResultset() {
        return resultset;
    }

    public void setResultset(NCDCResultSet resultset) {
        this.resultset = resultset;
    }

    @Override
    public String toString() {
        return resultset.toString();
    }
}

package meadowbrook.weather.external.ncdc.model;

import java.util.List;

/**
 * Top-Level object in NCDC JSON model.
 */
public class NCDCWeatherServiceData {

    private NCDCMetadata metadata;
    private List<NCDCReading> results;

    public NCDCMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(NCDCMetadata metadata) {
        this.metadata = metadata;
    }

    public List<NCDCReading> getResults() {
        return results;
    }

    public void setResults(List<NCDCReading> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return getMetadata() + ":" + getResults();
    }

}

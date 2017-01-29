package meadowbrook.weather.external.ncdc.model;

/**
 * NCDC JSON model.
 */
public class NCDCResultSet {
    private Long offset;
    private Long count;
    private Long limit;

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public String toString() {
        return "offset:" + offset + ", count:" + count + ", limit:" + limit;
    }
}

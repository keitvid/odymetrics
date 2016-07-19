package metrics;

/**
 * Created by IPermyakova on 07.07.2016.
 */
public class CmetricNulls extends Metrics {

    public CmetricNulls(String col_name) {
        super(col_name);
        this.sql_format = "select count(*) from %s.%s where " + col_name + " is null";
    }

    @Override
    public String getInfo() {
        return "[" + data.data_long +"] -> Nulls in " + col_name;
    }

    public Long getLongValue() throws MetricsTypeException {
        if (this.data.getType() == MetricsData.MetricType.TYPE_LONG) {
            return data.data_long;
        } else {
            throw new MetricsTypeException("Incorrect data type");
        }

    }
}


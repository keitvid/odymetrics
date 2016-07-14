package metrics;

/**
 * Created by IPermyakova on 07.07.2016.
 */
public class CmetricUniqueValuesForTable extends Metrics {

    public CmetricUniqueValuesForTable(String col_name) { super(col_name);
        this.sql_format = "select distinct count(*) from %s.%s";
    }

    @Override
    public String getInfo() {
        return "isEmpty: " + isEmpty + " CMetricUniqueValue " + data.data_long;
    }

    public Long getLongValue() throws MetricsTypeException {
        if(this.data.getType() == MetricsData.MetricType.TYPE_LONG) {
            return this.data.getLong();
        }
        else {throw new MetricsTypeException("Incorrect data type");}
    }

}

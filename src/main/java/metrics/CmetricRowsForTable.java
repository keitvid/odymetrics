package metrics;

/**
 * Created by IPermyakova on 06.07.2016.
 */
public class CmetricRowsForTable extends Metrics {

    public CmetricRowsForTable(String col_name) {
        super(col_name);
        this.sql_format = "select count(*) from %s.%s";
    }

    @Override
    public String getInfo() {
        return "isEmpty: " + isEmpty + " cMetricRows:" + data.data_long;
    }

    public Long getLongValue() throws MetricsTypeException {
        if(this.data.getType() == MetricsData.MetricType.TYPE_LONG) {
            return this.data.getLong();
        }
        else {throw new MetricsTypeException("Incorrect data type");}
    }



}

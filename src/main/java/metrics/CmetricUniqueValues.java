package metrics;

/**
 * Created by IPermyakova on 07.07.2016.
 */
public class CmetricUniqueValues extends Metrics {

    public CmetricUniqueValues(String col_name) {
        super(col_name);
        this.sql_format = "select count(distinct " + col_name + ") from %s.%s";
    }

    @Override
    public String getInfo() {
        return "[" + data.data_long +"] -> Unique Values in " + col_name;
    }


    public Long getLongValue() throws MetricsTypeException {
        if(this.data.getType() == MetricsData.MetricType.TYPE_LONG) {
            return this.data.getLong();
        }
        else {throw new MetricsTypeException("Incorrect data type");}
    }


}

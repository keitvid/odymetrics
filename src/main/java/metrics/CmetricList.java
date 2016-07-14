package metrics;

import java.util.List;

/**
 * Created by IPermyakova on 08.07.2016.
 */
public class CmetricList extends Metrics {

    public CmetricList(String col_name) {
        super(col_name);
        this.sql_format = "select " + col_name + " from %s.%s limit 5";
    }

    @Override
    public String getInfo() {
        return "CmetricList " + data.data_list + " isEmpty " + isEmpty;
    }

    public List<String> getListValue() throws MetricsTypeException {
        if (this.data.getType() == MetricsData.MetricType.TYPE_LIST) {
            return this.getListValue();
        } else {
            throw new MetricsTypeException("Incorrect data type");
        }
    }

}

package metrics;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IPermyakova on 12.07.2016.
 */
public class MetricsData implements Serializable {
    Long data_long;
    List<String> data_list;
    String data_string;

    MetricType type;

    public MetricsData(Long data_long) {
        this.data_long = data_long;
        this.type = MetricType.TYPE_LONG;
    }

    public MetricsData(List<String> data_list){
        this.data_list = data_list;
        this.type = MetricType.TYPE_LIST;
    }

    public MetricsData(String data_string) {
        this.data_string = data_string;
        this.type = MetricType.TYPE_STRING;
    }
    public enum MetricType {
        TYPE_LONG, TYPE_LIST, TYPE_STRING
    }

    MetricType getType() {
        return type;
    }

    Long getLong() {
        return data_long;
    }

    private MetricsData() {}
}

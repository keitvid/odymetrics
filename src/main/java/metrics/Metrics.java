package metrics;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IPermyakova on 06.07.2016.
 */
public abstract class Metrics implements Serializable {
    protected String col_name;
    protected Boolean isEmpty;
    protected String sql_format;
    protected MetricsData data;

    static AtomicLong nextId = new AtomicLong();
    private Long id;


    public Metrics(String col_name) {
        id = nextId.incrementAndGet();
        this.col_name = col_name;
        isEmpty = true;
    }

    public String getQuery(String schema_name, String table_name)
    {
        return String.format(sql_format, schema_name, table_name, col_name);
    }

    public MetricsData getData(){return data;};

    public void setData(MetricsData data){
        this.data = data;
        this.isEmpty = false;
    };

    public abstract String getInfo();

    public  Boolean isEmpty() {
        return isEmpty;
    }

    public Long GetId(){return id;}
}

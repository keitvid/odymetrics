package dbService;

import metrics.Metrics;

/**
 * Created by IPermyakova on 11.07.2016.
 */
public interface IMetricsVisitor {
    void visit(Metrics metrics, String schema_name, String table_name) throws DBException, ColumnException;

}

package dbService.dao;

import dbService.ColumnException;
import dbService.executor.Executor;
import dbService.executor.ResultHandler;
import metrics.MetricsData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class Request <T> {

    private Executor executor;
    private String query;

    public Request(Connection connection, String query) {
        this.executor = new Executor(connection);
        this.query = query;
    }

    public MetricsData execute() throws SQLException, ColumnException {

        return executor.execQuery(query,
                new ResultHandler<MetricsData>() {

                    public MetricsData handle(ResultSet result) throws SQLException, ColumnException {
                        boolean isMoreThanOneRow = result.first() && result.next();

                        result.beforeFirst();
                        //System.out.println(result.isFirst());
                        MetricsData metricsData = null;

                        ResultSetMetaData resMetaData = result.getMetaData();
                        String columnTypeStr = resMetaData.getColumnTypeName(1);                                          //varchar
                        int columnTypeInt = resMetaData.getColumnType(1);

                        if (resMetaData.getColumnCount() > 1 || resMetaData.getColumnCount() < 1) {
                            throw new ColumnException("Invalid query. Invalid number of columns");
                        }
                        if (resMetaData.getColumnCount() == 1) {
                            if (isMoreThanOneRow) {
                                List<String> lstr = new ArrayList<String>();
                                while (result.next()) {
                                    String value = result.getString(1);
                                    lstr.add(value);
                                }
                                metricsData = new MetricsData(lstr);
                            } else {
                                if (columnTypeStr == "int8") {
                                    result.next();
                                    metricsData = new MetricsData(result.getLong(1));
                                } else if (columnTypeStr == "varchar") {
                                    result.next();
                                    metricsData = new MetricsData(result.getString(1));
                                }
                            }
                        }

                        return metricsData;
                    }
                });

    }

}

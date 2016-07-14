package dbService;

import dbService.dao.Request;
import metrics.Metrics;
import metrics.MetricsData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import static main.Main.Log;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class DBService <V> implements IMetricsVisitor{
    private final Connection connection;

    public DBService(String dbUrl, String username) {
        this.connection = getRedConnection(dbUrl, username);
    }

    public void close () {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void visit(Metrics metrics, String schema_name, String table_name) throws DBException, ColumnException {

        try {
            String query = metrics.getQuery(schema_name, table_name);
            Request request = new Request(connection, query);
            MetricsData metricsData = request.execute();
            metrics.setData(metricsData);

        } catch (SQLException e) {
            throw new DBException(e);
        }

    }

    public void printConnectInfo() {
        try {
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("UnusedDeclaration")

    protected static Connection getRedConnection(String dbUrl, String username) {
        try {
            Driver driver = (Driver) Class.forName("org.postgresql.Driver").newInstance();
            StringBuilder url = new StringBuilder();
            PropertiesContainer prop = new PropertiesContainer();

            url.append("jdbc:postgresql://"). //FIXME Magic number
                append(dbUrl);

            //Promt user for password here

            Log("Please enter the password for user " + username + " on " + dbUrl);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String a = br.readLine();

            Connection connection = DriverManager.getConnection(url.toString(), username, a);

            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e)  {
            e.printStackTrace();
        }  catch (IllegalAccessException e)  {
            e.printStackTrace();
        }  catch (ClassNotFoundException e)  {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package main;

import builder.BuilderTB;
import builder.TableProduct;
import dbService.DBService;
import metrics.Metrics;
import metrics.SerializeHelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Iterator;

import static main.Main.DEBUG;
import static main.Main.Log;

/**
 * Created by FLisochenko on 14.07.2016.
 */
public class MainMetricRun {

    protected BuilderTB builderTB = new BuilderTB();
    protected DBService dbService;

    public MainMetricRun(DbCredentials obj)
    {
        builderTB.addCredentials(obj);
        dbService = new DBService(obj.getDbName(), obj.getUserName());
    }

    public void initializeProductFromFile(){
        Log("Adding the following metrics for the run:");
        builderTB.printThemAll();
        builderTB.populateMetrics();
    }

    public void initializeProductManually() {
        //This is a place where you can add any Metrics to product ahead of the ones you added from file

        if(DEBUG) {
            Log("");
            Log(builderTB.getProduct().toString());
            Log("");
        }
    }

    public void performRun() throws Exception { //TODO implement exception

        if(builderTB.isProductEmpty()) {
            throw new Exception("Product has no metrics");
        }

        TableProduct tableProduct = builderTB.getProduct();

        tableProduct.setDate();

        Long rowsCount = dbService.executeQuery(
                String.format("select count(*) from %s.%s",
                tableProduct.getCredentials().getSchemaName(),
                tableProduct.getCredentials().getTableName())
        );

        tableProduct.setRowsCount(rowsCount);

        Long uniqueRowsCount = dbService.executeQuery(
                String.format("select distinct count(*) from %s.%s",
                tableProduct.getCredentials().getSchemaName(),
                tableProduct.getCredentials().getTableName())
        );

        tableProduct.setUniqueRowsCount(uniqueRowsCount);

        if(DEBUG){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            Log("Table:             " + tableProduct.getCredentials().getSchemaName() + "." + tableProduct.getCredentials().getTableName());
            Log("Date:              " + tableProduct.getDate());
            Log("Rows count:        " + tableProduct.getRowsCount());
            Log("Unique Rows count: " + tableProduct.getUniqueRowsCount());
            Log("Has Duplicates:    " + tableProduct.hasDuplicates().toString());
            Log("");
        }

        Iterator<Metrics> itr = tableProduct.getMetricsIterator();

        while(itr.hasNext())
        {
            Metrics currentEntry = itr.next();

            dbService.visit(
                    currentEntry,
                    tableProduct.getCredentials().getSchemaName(),
                    tableProduct.getCredentials().getTableName()
            );

            if(DEBUG) {
                Log(currentEntry.getInfo());
            }
        }
    }

    public void saveDataToDb() throws Exception { //TODO Implement Exception

        if(builderTB.isProductEmpty()) {
            throw new Exception("Product has no metrics");
        }

        TableProduct tableProduct = builderTB.getProduct();

        SessionIdentifierGenerator sessionGenerator = new SessionIdentifierGenerator();

        String sessionId = sessionGenerator.nextSessionId();

        //Create a row with a specified random sessionId
        String sql = "INSERT INTO %s.%s (table_name) VALUES ('%s');";

        String formatted_query = String.format(sql,
                tableProduct.getCredentials().getSchemaName(),
                "odymetrics_run",
                sessionId);

        dbService.executeUpdate(formatted_query);

        //Get the generated IDENTITY field
        //This workaround should be fixed then redshift will support identity return from insert
        Long runId = dbService.executeQuery(
                String.format("select run_id from %s.%s where table_name = '%s'",
                        tableProduct.getCredentials().getSchemaName(),
                        "odymetrics_run",
                        sessionId)
        );

        if(DEBUG) {
            Log("SessionId: " + sessionId + " -> " + runId);
        }

        //Push table info into odymetrics_run table

        sql = "UPDATE %s.%s SET " +
                "table_name = '%s'" +
                ", run_time = '%s'" +
                ", rows = %d" +
                ", unique_rows = %d" +
                ", has_duplicates = '%s'" +
                " WHERE run_id = %d;";

        formatted_query = String.format(sql,
                tableProduct.getCredentials().getSchemaName(),
                "odymetrics_run",
                tableProduct.getCredentials().getTableName(),
                tableProduct.getDate(),
                tableProduct.getRowsCount(),
                tableProduct.getUniqueRowsCount(),
                tableProduct.hasDuplicates().toString(),
                runId
                );

        dbService.executeUpdate(formatted_query);

        //Push metrics into the odymetrics_columns table

        sql = "INSERT INTO %s.%s (data, run_id) VALUES (?, ?)";

        formatted_query = String.format(sql,
                tableProduct.getCredentials().getSchemaName(),
                "odymetrics_columns"
                );

        Iterator<Metrics> itr = tableProduct.getMetricsIterator();

        Base64.Encoder encoder = Base64.getEncoder();

        while(itr.hasNext())
        {
            Metrics currentEntry = itr.next();
            byte [] bytes_out = SerializeHelper.serialize(currentEntry);

            try {
                PreparedStatement statement = dbService.prepareStatement(formatted_query);

                String encodedString = encoder.encodeToString(bytes_out);

                if(DEBUG) {
                    Log("Pushing [" + encodedString.length() + "] chars to server");
                }

                statement.setString(1, encodedString);
                statement.setInt(2, (int) (long) runId);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private MainMetricRun(){}
}

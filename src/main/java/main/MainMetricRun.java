package main;

import builder.BuilderTB;
import builder.TableProduct;
import dbService.DBService;
import metrics.Metrics;

import java.util.Iterator;

import static main.Main.Log;
import static main.Main.DEBUG;

/**
 * Created by FLisochenko on 14.07.2016.
 */
public class MainMetricRun {

    protected BuilderTB builderTB = new BuilderTB();

    public MainMetricRun(DbCredentials obj){
        builderTB.addCredentials(obj);
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
        DBService dbService = new DBService(tableProduct.getCredentials().getDbName(),
                                            tableProduct.getCredentials().getUserName());

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
            Log("Date:              " + tableProduct.getDate().toString());
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

    private MainMetricRun(){}
}

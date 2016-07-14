package main;

import builder.BuilderTB;
import builder.TableProduct;
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
        }
    }

    public void fetchDataToProduct() throws Exception{

        if(builderTB.isProductEmpty()) {
            throw new Exception("Product has no metrics");
        }

        TableProduct tableProduct = builderTB.getProduct();

        Iterator<Metrics> itr = tableProduct.getMetricsIterator();

        while(itr.hasNext())
        {
            Metrics currentEntry = itr.next();

            Log(currentEntry.getQuery(tableProduct.getCredentials().getSchemaName(), tableProduct.getCredentials().getTableName()));
        }
    }

    private MainMetricRun(){}
}

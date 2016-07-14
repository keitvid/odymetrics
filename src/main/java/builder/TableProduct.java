package builder;

import main.DbCredentials;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by FLisochenko on 14.07.2016.
 */
public class TableProduct {
    private List<Metrics> listOfMetrics;
    private DbCredentials dbCredentials;

    public void addMetric(Metrics obj){
        listOfMetrics.add(obj);
    }

    public Boolean isEmpty(){return listOfMetrics.isEmpty();}

    public TableProduct() {
        listOfMetrics = new ArrayList<Metrics>();
    }

    public void addCredentials(DbCredentials obj) {
        dbCredentials = obj;
    }

    public DbCredentials getCredentials(){
        return dbCredentials;
    }

    public Iterator<Metrics> getMetricsIterator(){return listOfMetrics.iterator();}

    @Override
    public String toString() {
        return super.toString() + " : " + listOfMetrics;
    }
}

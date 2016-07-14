package builder;

import main.DbCredentials;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FLisochenko on 14.07.2016.
 */
public class TableProduct {
    private List<Metrics> listOfMetrics;
    private DbCredentials dbCredentials;

    void addMetric(Metrics obj){
        listOfMetrics.add(obj);
    }

    public TableProduct() {
        listOfMetrics = new ArrayList<Metrics>();
    }

    void addCredentials(DbCredentials obj) {
        dbCredentials = obj;
    }

    DbCredentials getCredentials(){
        return dbCredentials;
    }

    @Override
    public String toString() {
        return super.toString() + " : " + listOfMetrics;
    }
}

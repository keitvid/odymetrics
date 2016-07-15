package builder;

import main.DbCredentials;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by FLisochenko on 14.07.2016.
 */
public class TableProduct {
    private List<Metrics> listOfMetrics;
    private DbCredentials dbCredentials;
    private Date date = new Date();
    private Long rowsCount = 0l;
    private Long uniqueRowsCount = 0l;

    public Long getUniqueRowsCount() {
        return uniqueRowsCount;
    }

    public void setUniqueRowsCount(Long uniqueRowsCount) {
        this.uniqueRowsCount = uniqueRowsCount;
    }

    public Long getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(Long rowsCount) {
        this.rowsCount = rowsCount;
    }

    public Boolean hasDuplicates() {
        return (rowsCount == uniqueRowsCount);
    }

    public void addMetric(Metrics obj){listOfMetrics.add(obj);}
    public void setDate() {date = new Date();}
    public Date getDate() {return date;}

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

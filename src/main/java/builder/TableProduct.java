package builder;

import main.DbCredentials;
import metrics.Metrics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by FLisochenko on 14.07.2016.
 */
public class TableProduct {
    private List<Metrics> listOfMetrics;
    private DbCredentials dbCredentials;
    private Calendar calendar;
    private Long rowsCount = 0l;
    private Long uniqueRowsCount = 0l;

    public Long getUniqueRowsCount() {
        return uniqueRowsCount;
    }

    public void setUniqueRowsCount(Long uniqueRowsCount) {
        if (uniqueRowsCount >= 0) {this.uniqueRowsCount = uniqueRowsCount;}
        else {throw new IllegalArgumentException("uniqueRowsCount less or equals 0");}
    }

    public Long getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(Long rowsCount) {
        if(rowsCount > 0) {this.rowsCount = rowsCount;}
        else {throw new IllegalArgumentException("rowsCount less or equals 0");}

    }

    public Boolean hasDuplicates() {
        return (rowsCount == uniqueRowsCount);
    }

    public void addMetric(Metrics obj){
        try {
            if (obj == null){
                throw new NullPointerException("Bad object add");
            } else {
                listOfMetrics.add(obj);
            }
        } catch(NullPointerException e)  {
            e.printStackTrace();
            return;
        }
    }

    public void setDate() {calendar = Calendar.getInstance();}

    public String getDate() {
        try {
            return new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(calendar.getTimeInMillis());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Date did not set");
        }
    }
    public Boolean isEmpty(){
        return listOfMetrics.isEmpty() || dbCredentials.isEmpty();
    }

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
        return "\nDatabase name: " + dbCredentials.getDbName().split("/")
                + "\nSchema name: " + dbCredentials.getSchemaName()
                + "\nTable name: " + dbCredentials.getTableName()
                + "\nUser name: " + dbCredentials.getUserName()
                + "\nProp file path: " + dbCredentials.getPropFilePath()
                + "\nRows count: " + rowsCount
                + "\nUnique Rows count: " + uniqueRowsCount
                + "\nHas Dublicates: " + hasDuplicates();
    }
}

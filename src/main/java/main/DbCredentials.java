package main;

import java.util.ArrayList;

/**
 * Created by FLisochenko on 14.07.2016.
 */
final public class DbCredentials {

    public String getDbName() {
        return dbName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getTableName(int paramNumber) {
        return tableNamesList.get(paramNumber);
    }

    public String getUserName() {
        return userName;
    }

    public String getPropFilePath(int paramNumber) {
        return propFilePathsList.get(paramNumber);
    }

    public ArrayList<String> getPropFilePathsList() {
        return propFilePathsList;
    }

    private String dbName;
    private String schemaName;
    private String userName;
    ArrayList<String> tableNamesList = new ArrayList<String>();
    ArrayList<String> propFilePathsList = new ArrayList<String>();

    public DbCredentials(String dbName, String schemaName, ArrayList<String> tableNamesList, String userName, ArrayList<String> propFilePathsList){
        this.dbName = dbName;
        this.schemaName = schemaName;
        this.userName = userName;
        this.tableNamesList = tableNamesList;
        this.propFilePathsList = propFilePathsList;
    }

    public DbCredentials(){} //TODO add null creds flag and throw exception?
}

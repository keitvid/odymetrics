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

    public String getTableName() {
        return tableName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPropFilePath() {
        return propFilePath;
    }

    private String dbName;
    private String schemaName;
    private String userName;

    private String tableName;
    private String propFilePath;

    public DbCredentials(String dbName, String schemaName, String tableName, String userName, String propFilePath){
        this.dbName = dbName;
        this.schemaName = schemaName;
        this.userName = userName;
        this.tableName = tableName;
        this.propFilePath = propFilePath;
    }

    public DbCredentials(){} //TODO add null creds flag and throw exception?
}

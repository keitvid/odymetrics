package main;

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
    private String tableName;
    private String userName;
    private String propFilePath;

    public DbCredentials(String dbName, String schemaName, String tableName, String userName, String propFilePath){
        this.dbName = dbName;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.userName = userName;
        this.propFilePath = propFilePath;
    }

    public boolean isEmpty() {
        return dbName.equals("") || schemaName.equals("") || tableName.equals("") || userName.equals("")
                || propFilePath.equals("");
    }
    public DbCredentials(){} //TODO add null creds flag and throw exception?
}

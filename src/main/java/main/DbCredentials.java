package main;

/**
 * Created by FLisochenko on 14.07.2016.
 */
final public class DbCredentials {

    public String getDbName() {
        if (!dbName.equals("")) {
            return dbName;
        } else {
            throw new IllegalArgumentException("Database name is not populated");
        }
    }

    public String getSchemaName() {
        if (!schemaName.equals("")) {
            return schemaName;
        } else {
            throw new IllegalArgumentException("Schema name is not populated");
        }
    }

    public String getTableName() {
        if (!tableName.equals("")) {
            return tableName;
        } else {
            throw new IllegalArgumentException("Table name is not populated");
        }
    }

    public String getUserName() {
        if (!userName.equals("")) {
            return userName;
        } else {
            throw new IllegalArgumentException("User name is not populated");
        }
    }

    public String getPropFilePath() {
        if (!propFilePath.equals("")) {
            return propFilePath;
        } else {
            throw new IllegalArgumentException("Prop file path is not populated");
        }
    }

    private String dbName;
    private String schemaName;
    private String tableName;
    private String userName;
    private String propFilePath;

    public DbCredentials(String dbName, String schemaName, String tableName, String userName, String propFilePath) {
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

    public DbCredentials() {
    } //TODO add null creds flag and throw exception?
}

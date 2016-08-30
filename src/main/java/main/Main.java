package main;

import java.util.*;

/**
 * Created by FLisochenko on 13.07.2016.
 */
public class Main {

    private static List<String> argsList = new ArrayList<String>();
    private static HashMap<String, String> optsHash = new HashMap<String, String>();
    private static List<String> doubleOptsList = new ArrayList<String>();
    private static ArrayList<DbCredentials> dbCreds = new ArrayList<DbCredentials>();

    public static Boolean DEBUG = false;

    public static void Log(String str) {
        System.out.println(str);
    }

    private static void parseArgs(String[] args) throws IllegalArgumentException {
        for (int i = 0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    if (args[i].length() < 2)
                        throw new IllegalArgumentException("Not a valid argument: " + args[i]);
                    if (args[i].charAt(1) == '-') {
                        if (args[i].length() < 3)
                            throw new IllegalArgumentException("Not a valid argument: " + args[i]);
                        // --opt
                        doubleOptsList.add(args[i].substring(2, args[i].length()));
                    } else {
                        if (args.length - 1 == i)
                            throw new IllegalArgumentException("Expected arg after: " + args[i]);
                        // -opt
                        optsHash.put(args[i], args[i + 1]);
                        i++;
                    }
                    break;
                default:
                    // arg
                    argsList.add(args[i]);
                    break;
            }
        }
    }

    private static ArrayList<DbCredentials> createCredsFromArgs() throws IllegalArgumentException {
        String dbName = "";
        String schemaName = "";
        ArrayList<String> tableNamesList = new ArrayList<String>(); //TODO fix this for multiple prop files
        ArrayList<String> propFilePathsList = new ArrayList<String>(); //TODO fix this for multiple prop files
        String userName = "";

        for (int i = 0; i < argsList.size(); i++){
            String param = argsList.get(i);
            String tblName = param.replace(".prop", "");
            propFilePathsList.add(param);
            tableNamesList.add(tblName);
        }
        Iterator it = optsHash.entrySet().iterator();

        while (it.hasNext())
        {
            Map.Entry curEntry = (Map.Entry) it.next();

            String key = (String) curEntry.getKey();
            String value = (String) curEntry.getValue();

            if (key.equals("-d"))
            {
                dbName = value;
            }
            else if (key.equals("-s"))
            {
                schemaName = value;
            }
            else if (key.equals("-u"))
            {
                userName = value;
            }
            else
            {
                throw new IllegalArgumentException("Unknown pair of arguments: " + key + " " + value);
            }
        }

        if (dbName.length() == 0 || schemaName.length() == 0 || tableNamesList.size() < 1 || userName.length() == 0 ||
                propFilePathsList.size() < 1)
        {
            throw new IllegalArgumentException("Not enough arguments");
        }

        ArrayList<DbCredentials> results = new ArrayList<DbCredentials>();

        for(int i = 0; i < tableNamesList.size(); i++) {
            results.add(new DbCredentials(dbName, schemaName, tableNamesList.get(i), userName, propFilePathsList.get(i)));
        }
        return results;
    }

    private static void printUsage() {
        Log("Usage: java -jar ody-metrics-1.0.jar [argument ...] metricsfile...");
        Log("Options:");
        Log("-d Database name");
        Log("-s Schema name");
        //Log("-t Table name");
        Log("-u User name");
        Log("-? --help Print this help list");
        Log("--debug Toggle debug mode");
    }

    public static void main(String[] arg) throws Exception {

        if (arg.length == 0 || arg[0].equals("-?") || arg[0].equals("--help")) {
            printUsage();
            return;
        }

        try
        {
            parseArgs(arg); //TODO: make arg parsing more accurate and fault-tolerant
            if(!doubleOptsList.isEmpty() && doubleOptsList.get(0).equals("debug")) {
                DEBUG = true;
            }
            dbCreds = createCredsFromArgs();
        }
        catch (IllegalArgumentException e) {
            Log(e.getMessage());
            printUsage();
        }

        for(int i = 0; i < dbCreds.size(); i++) {

            MainMetricRun run = new MainMetricRun(dbCreds.get(i));

            run.initializeProductFromFile();
            run.initializeProductManually();
            run.performRun();
            run.saveDataToDb();
        }
    }
}

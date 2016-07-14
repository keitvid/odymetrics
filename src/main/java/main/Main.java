package main;

import java.util.*;

/**
 * Created by FLisochenko on 13.07.2016.
 */
public class Main {

    private static List<String> argsList = new ArrayList<String>();
    private static HashMap<String, String> optsHash = new HashMap<String, String>();
    private static List<String> doubleOptsList = new ArrayList<String>();
    private static DbCredentials dbCreds = null;

    public static Boolean DEBUG = false;

    public static void Log(String str) {
        System.out.println(str);
    }

    private static void parseArgs( String[] args) throws IllegalArgumentException {
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

    private static DbCredentials createCredsFromArgs() throws IllegalArgumentException {
        String dbName = "";
        String schemaName = "";
        String tableName = "";
        String userName = "";
        String propFilePath = argsList.get(0); //TODO fix this for multiple prop files

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
            else if (key.equals("-t"))
            {
                tableName = value;
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

        if (dbName.length() == 0 || schemaName.length() == 0 || tableName.length() == 0 || userName.length() == 0 ||
                propFilePath.length() == 0)
        {
            throw new IllegalArgumentException("Not enough arguments");
        }

        return new DbCredentials(dbName, schemaName, tableName, userName, propFilePath);
    }

    private static void printUsage() {
        Log("Usage: java -jar ody-metrics-1.0.jar [argument ...] metricsfile...");
        Log("Options:");
        Log("-d Database name");
        Log("-s Schema name");
        Log("-t Table name");
        Log("-u User name");
        Log("-? --help Print this help list");
        Log("--debug Toggle debug mode");
    }

    public static void main(String[] arg) throws Exception {

        if (arg[0].equals("-?") || arg[0].equals("--help")) {
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

        MainMetricRun run = new MainMetricRun(dbCreds);

        run.initializeProductFromFile();
        run.initializeProductManually();
        run.fetchDataToProduct();

        /*
        Log("");

        String path = "d:\\sources\\ohdsi\\trash\\";
        for (Metrics obj: metrics) {
            byte [] bytes_out = SerializeHelper.serialize(obj);
            String filename = obj.getClass().getSimpleName() + Long.toHexString(obj.getId()) + ".ser";
            FileOutputStream fos = new FileOutputStream(path + filename);
            fos.write(bytes_out);
            fos.close();
        }
        */
    }
}

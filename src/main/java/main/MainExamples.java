package main;

import metrics.CmetricNulls;
import metrics.CmetricUniqueValues;
import metrics.Metrics;
import metrics.SerializeHelper;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by FLisochenko on 13.07.2016.
 */
public class MainExamples {

    protected static void Log(String str) {
        System.out.println(str);
    }

    protected static Connection getConnection(String dbUrl, String username) {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            StringBuilder url = new StringBuilder();

            url.append("jdbc:postgresql://").append(dbUrl); //FIXME Magic number

            //Promt user for password here
            Log("Please enter the password for user " + username + " on " + dbUrl + " :");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String a = br.readLine();

            return DriverManager.getConnection(url.toString(), username, a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*This is example of deserialization from db*/
    public static void main_db(String[] arg) throws Exception {
        Connection connection = getConnection("fls-playground.cq2hhifgzij7.us-west-2.redshift.amazonaws.com:5439/thindb", "flsdev");

        Statement statement = connection.createStatement();

        statement.execute("select data from thin_ref_omop_v4_etl.odymetrics_columns");

        ArrayList<String> strData = new ArrayList<String>();
        ResultSet result = statement.getResultSet();
        while(result.next()){
            strData.add(result.getString("data"));
        }

        Base64.Decoder decoder = Base64.getDecoder();
        ArrayList<Metrics> metricsArray = new ArrayList<Metrics>();

        Log("Data:");
        for (String s: strData) {
            Log(s);
            metricsArray.add(SerializeHelper.deserialize(decoder.decode(s)));
        }

        Log("");
        Log("Metrics:");
        for (Metrics m: metricsArray) {
            Log(m.getInfo());
        }

        statement.close();
    }

    /*This is example of serialization and deserialization to file*/
    public static void main_file(String[] arg) throws Exception{

        Metrics obj1 = new CmetricNulls("CmetricNulls");
        Metrics obj2 = new CmetricUniqueValues("CmetricUniqueValues");

        Log("Original objects data:");

        Log(obj1.toString());
        Log(obj2.toString());

        byte [] obj1_bytes_out = SerializeHelper.serialize(obj1);
        byte [] obj2_bytes_out = SerializeHelper.serialize(obj2);

        FileOutputStream fos1 = new FileOutputStream("first_object.ser");
        FileOutputStream fos2 = new FileOutputStream("second_object.ser");

        fos1.write(obj1_bytes_out);
        fos2.write(obj2_bytes_out);

        fos1.close();
        fos2.close();

        File file1 = new File("first_object.ser");
        File file2 = new File("second_object.ser");
        FileInputStream fis1 = new FileInputStream(file1);
        FileInputStream fis2 = new FileInputStream(file2);

        byte obj1_bytes_in [] = new byte[(int)file1.length()];
        byte obj2_bytes_in [] = new byte[(int)file2.length()];

        fis1.read(obj1_bytes_in);
        fis2.read(obj2_bytes_in);

        Metrics obj1_des = SerializeHelper.deserialize(obj1_bytes_in);
        Metrics obj2_des = SerializeHelper.deserialize(obj2_bytes_in);

        Log("");

        Log("Deserialized objects data:");

        Log(obj1_des.toString());
        Log(obj2_des.toString());
    }

}

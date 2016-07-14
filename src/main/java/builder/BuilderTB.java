package builder;
/**
 * Created by DGusenkov on 08.07.2016.
 */
import main.DbCredentials;
import metrics.Metrics;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Properties;

public class BuilderTB {

    TableProduct tableProduct = new TableProduct();

    public TableProduct getProduct() {return tableProduct;}

    public Boolean isProductEmpty(){return getProduct().isEmpty();}

    public void addCredentials(DbCredentials creds) {
        getProduct().addCredentials(creds); //TODO add flag check
    }

    public Metrics createClasses(String className, String columnName) throws ClassNotFoundException {

        Metrics metric = null;
        Constructor c;
        try {
            c = Class.forName("metrics." + className).getConstructor(String.class); //FIXME magic word

            metric = (Metrics) c.newInstance(columnName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return metric;
    }

    public void populateMetrics() {

        String fileName = getProduct().getCredentials().getPropFilePath(); //FIXME this should be safe
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = getClass().getClassLoader().getResourceAsStream(fileName);
            if (input == null) {
                System.out.println("Sorry, unable to find " + fileName);
                return;
            }

            prop.load(input);

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String property = (String) e.nextElement();
                String[] parts = property.split("-");
                String columnName = parts[0];
                String classType = parts[1];
                try {
                    getProduct().addMetric(createClasses(classType, columnName));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void printThemAll() {

        String fileName = getProduct().getCredentials().getPropFilePath(); //FIXME this should be safe as well
        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = fileName;
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            prop.load(input);

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String property = (String) e.nextElement();
                String[] parts = property.split("-");
                String key = parts[0];
                String value = parts[1];

                System.out.println(key + " : " + value);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}

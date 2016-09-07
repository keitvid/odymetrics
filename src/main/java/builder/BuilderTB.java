package builder;
/**
 * Created by DGusenkov on 08.07.2016.
 */

import main.DbCredentials;
import metrics.Metrics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Properties;

public class BuilderTB {
    public static final String PACKAGE_NAME = "metrics.";

    TableProduct tableProduct = new TableProduct();

    public TableProduct getProduct() {return tableProduct;}

    public Boolean isProductEmpty(){return getProduct().isEmpty();}

    public void addCredentials(DbCredentials creds) {

        if (!creds.isEmpty()) {
            getProduct().addCredentials(creds);
        } else {
            throw new IllegalArgumentException("Credentials are not populated");
        }
    }

    public Metrics createClasses(String className, String columnName) throws ClassNotFoundException {

        Metrics metric = null;
        Constructor c;
        try {
            c = Class.forName(PACKAGE_NAME + className).getConstructor(String.class);
            metric = (Metrics) c.newInstance(columnName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return metric;
    }

    public void populateMetrics() {

        String fileName = getProduct().getCredentials().getPropFilePath();
        Properties prop = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input != null) {
                prop.load(input);
            } else {
                throw new FileNotFoundException("property file " + fileName + " not found");
            }

            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String property = (String) e.nextElement();
                String[] parts = property.split("-");
                String columnName = parts[0];
                String classType = parts[1];
                getProduct().addMetric(createClasses(classType, columnName));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}

package dbService;

import java.io.*;
import java.util.Properties;

/**
 * Created by IPermyakova on 08.06.2016.
 */
public class PropertiesContainer {
    String result = "";
    InputStream inputStream;

    public Properties getPropValues() {
        Properties prop = new Properties();
        try {
            String propFileName = System.getProperty("user.home") + "/resources/config.properties";
            File file = new File(propFileName);

            inputStream = new FileInputStream(file);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file " + propFileName + "not found in the classpath");
            }

        } catch (IOException e) {
            System.out.println("Exeption: " + e);
        }
        return prop;
    }

}

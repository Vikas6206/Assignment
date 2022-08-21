package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private Properties prop;

    public PropertiesReader() {

        prop = new Properties();

        try {
            String propertiesFilePath = "local.properties";
            InputStream inputStream = getResourceAsStream(propertiesFilePath);
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream getResourceAsStream(String path) {
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }

    public Properties getProperties() {
        return prop;
    }

    public String getHost() {
        return prop.getProperty("host");
    }
}

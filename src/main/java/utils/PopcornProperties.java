package utils;

public class PopcornProperties {
    private static final PropertiesReader propertiesReader = new PropertiesReader();
    public static final String hostLink = propertiesReader.getHost();
    public static final String language = null == System.getProperty("language") ? "en-US" : System.getProperty("language");
}

package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in resources folder");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load config.properties", ex);
        }
    }

    public static String getWebsite() {
        return properties.getProperty("demo_website");
    }

    public static String getSampleFile() {
        return properties.getProperty("sample_file_path");
    }

    public static boolean getHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless"));
    }
}


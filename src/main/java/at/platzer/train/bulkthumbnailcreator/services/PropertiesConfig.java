package at.platzer.train.bulkthumbnailcreator.services;

import at.platzer.train.bulkthumbnailcreator.api.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfig implements Config {

    private Properties properties;

    public PropertiesConfig() {

    }

    public void LoadConfig(String propFilePath){
        if(propFilePath == null || propFilePath.isBlank())
            throw new IllegalArgumentException("propFilePath cannot be empty");

        try (InputStream input = new FileInputStream(propFilePath)) {
            properties = new Properties();
            // load a properties file
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getSourceImageFolderPath() {
        return this.properties.getProperty("source.imagefolderpath");
    }

    @Override
    public String getTargetThumbnailFolderPath() {
        return this.properties.getProperty("target.thumbnailfolderpath");
    }
}

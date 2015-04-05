package tutor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by user on 12.12.2014.
 */
public class ConfigHelper extends ConfigKeys{

    private ConfigHelper(){
    }

    private static ConfigHelper instance = null;
    private Properties configFile = null;
    private static final String CONFIG_FILE_PATH = "config.properties";

    public static ConfigHelper getInstance(){
        if (instance == null) {
            instance = new ConfigHelper();
            instance.configFile = new Properties();
            try {
                File configFile = new File(CONFIG_FILE_PATH);
                if (!configFile.exists()){
                    configFile.createNewFile();
                }
                instance.configFile.load(new FileInputStream(CONFIG_FILE_PATH));
                instance.loadDefaultSettings();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return instance;
    }

    public String getParameter(String key){

        return instance.configFile.getProperty(key);
    }

    private void setParameter(String key, String value){
        getInstance().configFile.setProperty(key,value);
        try {
            getInstance().configFile.store(new FileOutputStream(CONFIG_FILE_PATH), null);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void loadDefaultSettings(){
        setParameter(THEME_FROST, "themes/frost/frost.css");
        setParameter(LANGUAGE, "en");
    }
}

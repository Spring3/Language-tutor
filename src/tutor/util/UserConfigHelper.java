package tutor.util;

import java.io.*;
import java.util.Properties;

/**
 * Created by user on 13.12.2014.
 */
public class UserConfigHelper extends ConfigKeys{
    private UserConfigHelper(){
    }

    private static UserConfigHelper instance = null;
    private Properties userConfigFile = null;
    private static final String CONFIG_FILE_PATH = "user_config.properties";

    public static UserConfigHelper getInstance(){
        if (instance == null){
            boolean isPropertyFileNew = false;
            instance = new UserConfigHelper();
            instance.userConfigFile = new Properties();
            try {
                File userConfigFile = new File(CONFIG_FILE_PATH);
                if (!userConfigFile.exists()){
                    userConfigFile.createNewFile();
                    isPropertyFileNew = true;
                }
                instance.userConfigFile.load(new FileInputStream(CONFIG_FILE_PATH));
                if (isPropertyFileNew){
                    instance.loadDefaultSettings();
                }
            }
            catch (FileNotFoundException ex){
                ex.printStackTrace();
            }
            catch (IOException ioex){
                ioex.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Sets parameter with key and value to user_config.cfg file.
     * @return
     */
    public void setParameter(String key, String value){
        getInstance().userConfigFile.setProperty(key,value);
        try{
            getInstance().userConfigFile.store(new FileOutputStream(CONFIG_FILE_PATH), null);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Gets parameter by key from user_config.cfg.
     * @return
     */
    public String getParameter(String key){
       return getInstance().userConfigFile.get(key).toString();
    }

    private void loadDefaultSettings(){
        setParameter(THEME_FROST, ConfigHelper.getInstance().getParameter(THEME_FROST));
        setParameter(LANGUAGE, ConfigHelper.getInstance().getParameter(LANGUAGE));
        setParameter(DATA_SOURCE, ConfigHelper.getInstance().getParameter(DATA_SOURCE));
        setParameter(DATA_SOURCE_URL, ConfigHelper.getInstance().getParameter(DATA_SOURCE_URL));
    }



}

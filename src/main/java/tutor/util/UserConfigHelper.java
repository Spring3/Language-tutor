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
    public static final String SELECTED_THEME = "SELECTED_THEME";

    public static UserConfigHelper getInstance(){
        if (instance == null){
            synchronized (UserConfigHelper.class) {
                if (instance == null) {
                    boolean isPropertyFileNew = false;
                    instance = new UserConfigHelper();
                    instance.userConfigFile = new Properties();
                    try {
                        File userConfigFile = new File(CONFIG_FILE_PATH);
                        if (!userConfigFile.exists()) {
                            userConfigFile.createNewFile();
                            isPropertyFileNew = true;
                        }
                        instance.userConfigFile.load(new FileInputStream(CONFIG_FILE_PATH));
                        if (isPropertyFileNew) {
                            instance.loadDefaultSettings();
                        }
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ioex) {
                        ioex.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Sets parameter with key and value to user_config.cfg file.
     * @return
     */
    public void setParameter(String key, String value){
        userConfigFile.setProperty(key,value);
        try{
            userConfigFile.store(new FileOutputStream(CONFIG_FILE_PATH), null);
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
        Object result = getInstance().userConfigFile.get(key);
        return result == null ? null : result.toString();
    }

    private void loadDefaultSettings(){
        setParameter(this.SELECTED_THEME, ConfigHelper.getInstance().getParameter(THEME_FROST));
        setParameter(LANGUAGE, ConfigHelper.getInstance().getParameter(LANGUAGE));
    }



}

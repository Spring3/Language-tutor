package tutor.controllers;

import java.io.IOException;
import java.net.URL;
import tutor.Main;

/**
 * Created by user on 19.12.2014.
 * Contains all the links to all the views, used within this software.
 * All paths must be relative!
 */
public final class Navigator {

    private Navigator(){

    }

    public static final String MAIN_VIEW_PATH = "view/main.fxml";
    public static final String TASKVIEW_DICTATION_PATH = "view/dictation.fxml";
    public static final String DICTIONARY_VIEW_PATH = "view/dictionary.fxml";
    public static final String USER_RATE_VIEW_PATH = "view/userRate.fxml";
    public static final String ABOUT_VIEW_PATH = "view/about.fxml";
    public static final String AUTHENTICATION_VIEW_PATH = "view/auth.fxml";
    public static final String FILE_IMPORT_VIEW_PATH = "view/fileImport.fxml";
    public static final String LANGUAGE_SETTINGS_VIEW_PATH = "view/languageSettings.fxml";
    public static final String THEME_SETTINGS_VIEW_PATH = "view/themeSettings.fxml";
    public static final String LOCALE_VIEW_PATH = "view/localeSettings.fxml";

    public static URL getPathFor(String view){
        return Main.class.getClassLoader().getResource(view);
    }
}

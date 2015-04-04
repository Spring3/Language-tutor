package tutor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tutor.controllers.AuthController;
import tutor.controllers.Controller;
import tutor.controllers.Navigator;
import tutor.util.DbManager;
import tutor.util.UserConfigHelper;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Class.forName("org.h2.Driver");
        //setting up database
        DbManager dbManager = DbManager.getInstance();
        if(dbManager.checkConnection()){
            System.out.println("Connection succeeded");
        }
        else System.out.println("Connection failed");
        //setting up locale to program's default
        Locale.setDefault(new Locale(UserConfigHelper.getInstance().getParameter(UserConfigHelper.LANGUAGE)));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Navigator.AUTHENTICATION_VIEW_PATH));
        loader.setResources(ResourceBundle.getBundle("locale/lang", Locale.getDefault()));
        loader.load();
        AuthController controller = loader.getController();
        controller.navigateTo(getClass().getResource(Navigator.AUTHENTICATION_VIEW_PATH),"Language Tutor", 0, false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

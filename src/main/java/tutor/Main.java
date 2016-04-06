package tutor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import tutor.controllers.AuthController;
import tutor.controllers.Navigator;
import tutor.util.DbManager;
import tutor.util.UserConfigHelper;
import tutor.util.Voice;

import java.net.URL;
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
        FXMLLoader loader = new FXMLLoader(Main.class.getClassLoader().getResource((Navigator.AUTHENTICATION_VIEW_PATH)));
        System.out.println(Locale.getDefault());
        loader.setResources(ResourceBundle.getBundle("locale/lang", Locale.getDefault()));
        Parent root = loader.load();
        AuthController controller = loader.getController();
        controller.navigateTo(Main.class.getClassLoader().getResource(Navigator.AUTHENTICATION_VIEW_PATH),"Language Tutor", 0, false);
        Voice.getInstance(); //to initialize maryTTS
    }


    public static void main(String[] args) {
        launch(args);
    }
}

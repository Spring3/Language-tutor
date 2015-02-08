package tutor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tutor.controllers.Controller;
import tutor.util.UserConfigHelper;

import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Locale.setDefault(new Locale(UserConfigHelper.getInstance().getParameter(UserConfigHelper.LANGUAGE)));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.navigateTo(getClass().getResource("view/main.fxml"),"Language Tutor", 0 );
    }


    public static void main(String[] args) {
        launch(args);

    }
}

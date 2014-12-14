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
        primaryStage.setTitle("Language Tutor");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setCurrentMainStage(primaryStage);
        Scene mainScene = new Scene(root);
        mainScene.getStylesheets().add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.SELECTED_THEME));
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);

    }
}

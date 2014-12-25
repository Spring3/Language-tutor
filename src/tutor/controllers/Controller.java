package tutor.controllers;

import com.sun.istack.internal.NotNull;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tutor.util.UserConfigHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main controller for the main.fxml view.
 */
public class Controller extends Navigator implements Initializable
{

    private Stage mainStage = null;
    private Parent mainRoot = null;
    private FXMLLoader mainLoader;
    private Scene mainScene = null;
    private Stage additionalStage = null;
    private Parent additionalRoot = null;
    private FXMLLoader additionalLoader = null;
    private Scene additionalScene = null;


    @Override
    public void initialize(URL url, ResourceBundle rb){
    }


    /**
     * Navigation dispatcher.
     * @param fxmlViewPath a path to the new view
     * @param title title for a new scene
     * @param isMain wheather it should be shown as a main stage or not.
     */
    private void navigateTo(@NotNull String fxmlViewPath, @NotNull String title, @NotNull boolean isMain) {
        if (isMain) {
            try {
                if (mainStage != null){
                    if (mainStage.getTitle().equals(title)){
                       return;
                    }
                }
                mainLoader = new FXMLLoader(getClass().getResource(fxmlViewPath));
                mainRoot = mainLoader.load();
                mainScene = new Scene(mainRoot);
                mainScene.getStylesheets().add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.SELECTED_THEME));
                if (mainStage != null)
                mainStage.close();
                mainStage = new Stage();
                mainStage.setScene(mainScene);
                mainStage.setTitle(title);
                mainStage.setOnHiding((WindowEvent windowEvent) -> {
                    mainStage = null;
                    System.out.println("Main stage was reseted");
                });
                mainStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            try {

                if (additionalStage != null) {
                    if (additionalStage.getTitle().equals(title)) {
                       return;
                    }
                }
                additionalLoader = new FXMLLoader(getClass().getResource(fxmlViewPath));
                additionalRoot = additionalLoader.load();
                additionalScene = new Scene(additionalRoot);
                additionalScene.getStylesheets().add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.SELECTED_THEME));
                if (additionalStage != null)
                    additionalStage.close();
                additionalStage = new Stage();
                additionalStage.setScene(additionalScene);
                additionalStage.setTitle(title);
                additionalStage.setOnHiding((WindowEvent windowEvent) -> {
                    additionalStage = null;
                    System.out.println("Additional stage was reseted");
                });
                additionalStage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Event handler for "Invite friend" menu item.
     * Opens a new stage.
     * @param actionEvent
     */
    public void InvitationMenuItemClicked(ActionEvent actionEvent) {
        navigateTo(INVITATION_VIEW_PATH, "Invite a friend via email" , false);
    }

    public void EditSettingsClick(ActionEvent actionEvent) {
        navigateTo(EDIT_SETTINGS_PATH, "Settings", false);
    }

    public void Shutdown(ActionEvent actionEvent) {
        mainStage.close();
    }

    public void setCurrentMainStage(@NotNull Stage stage){
        mainStage = stage;
    }

    public void setCurrentAdditionalStage (@NotNull Stage stage){
        additionalStage = stage;
    }




}

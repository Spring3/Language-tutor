package tutor.controllers;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tutor.util.GDriveManager;
import tutor.util.StageManager;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 26.02.2015.
 */
public class WebViewController implements Initializable {

    @FXML
    private WebView browser;

    private WebEngine webEngine;

    private GDriveManager gDriveManager;

    private static String codeString;

    public String getCode(){
        return codeString.substring(codeString.indexOf("=4/") +1, codeString.length() -1);
    }

    private StageManager stageManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stageManager = StageManager.getInstance();
        webEngine = browser.getEngine();
        gDriveManager = GDriveManager.getInstance(resources);
        webEngine.load(gDriveManager.getFlowURL());
        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            try {
                codeString = newValue;
                webEngine.getLoadWorker().stateProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1 == Worker.State.SUCCEEDED || newValue1 == Worker.State.FAILED) {
                        if (codeString.contains("oauth2callback?code")) {
                            GDriveManager.getInstance(resources).setCode(getCode());
                            stageManager.closeStage((Stage) browser.getScene().getWindow());
                        }
                    }
                });
            }
            catch (Exception ex){

            }
        });
    }
}

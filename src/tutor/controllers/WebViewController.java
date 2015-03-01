package tutor.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class WebViewController extends Navigator implements Initializable {

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
        stageManager = StageManager.getInstance(3);
        webEngine = browser.getEngine();
        gDriveManager = GDriveManager.getInstance();
        webEngine.load(gDriveManager.getFlowURL());
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    codeString = newValue;
                    webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                        @Override
                        public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                            if (newValue == Worker.State.SUCCEEDED || newValue == Worker.State.FAILED) {
                                if (codeString.contains("oauth2callback?code")) {
                                    System.out.println(getCode());
                                    GDriveManager.getInstance().setCode(getCode());
                                    stageManager.closeStage((Stage) browser.getScene().getWindow());
                                }
                            }
                        }
                    });
                }
                catch (Exception ex){

                }
            }
        });
    }
}

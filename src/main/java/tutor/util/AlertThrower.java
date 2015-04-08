package tutor.util;

import javafx.scene.control.Alert;

/**
 * Created by user on 05.04.2015.
 */
public final class AlertThrower {

    private static AlertThrower instance = null;

    private AlertThrower(){

    }


    public AlertThrower getInstance(){
        if (instance == null){
            synchronized (AlertThrower.class){
                if (instance == null){
                    instance = new AlertThrower();
                }
            }
        }
        return instance;
    }

    public void throwAlert(Alert.AlertType alertType, String alertTitle, String alertContent, String alertMessage){
        Alert alert = new Alert(alertType);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertContent);
        alert.setContentText(alertMessage);
        alert.show();

    }
}

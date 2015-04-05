package tutor.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by user on 08.02.2015.
 * Operates the amount of layers of Stages, that can be activated and shown at once.
 */
public class StageManager {


    private Map<Integer,URL> stagePaths;      //Integer = stage layer, URL = stage's fxml view path
    private Map<Integer,Stage> stages;
    private static final int MAX_LAYERS = 3;                   //max layer count
    private static StageManager instance;

    private StageManager(){
        stagePaths = new LinkedHashMap<>(MAX_LAYERS);
        stages = new LinkedHashMap<>(MAX_LAYERS);
    }


    public static StageManager getInstance() {
        if (instance == null) {
            synchronized (StageManager.class) {
                if (instance == null)
                    instance = new StageManager();
            }
        }
        return instance;
    }

    /**
     * Navigation dispatcher.
     * @param fxmlViewURL a URL to the new view
     * @param title title for a new scene
     * @param layerIndex is a layer of a stage to be shown on
     */
    public void navigateTo(URL fxmlViewURL, String title, int layerIndex, Optional<Boolean> isResizable) {
        navigateTo(fxmlViewURL, title, layerIndex, isResizable, false);
    }

    public void navigateTo(URL fxmlViewURL, String title, int layerIndex, Optional<Boolean> isResizable, boolean waitUntilCloses) {
        try {
            Stage stage = null;
            if (layerIndex > MAX_LAYERS || layerIndex < 0)
                return;
            if (stages.get(layerIndex) == null || stagePaths.get(layerIndex) == null) {
                addStage(bakeStage(fxmlViewURL, title, layerIndex, isResizable), fxmlViewURL, layerIndex);
            }
            stage = stages.get(layerIndex);
            String mainFMXLViewPath = stagePaths.get(layerIndex).toExternalForm();
            if (mainFMXLViewPath.equals(fxmlViewURL.toExternalForm())) {
                if (stage.isShowing())
                    return;
            } else {
                stage.close();
                addStage(bakeStage(fxmlViewURL, title, layerIndex, isResizable), fxmlViewURL, layerIndex);
                stage = stages.get(layerIndex);

            }
            stages.put(layerIndex, stage);
            stagePaths.put(layerIndex, fxmlViewURL);
            if (waitUntilCloses)
                stage.showAndWait();
            else
                stage.show();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    public void putStage(URL stagePath,Stage stage, int layer){
        if (layer > 0 && layer < MAX_LAYERS) {
            closeStage(stages.get(layer));
            stages.put(layer, stage);
            stagePaths.put(layer, stagePath);
            stages.get(layer).show();
        }
    }

    /**
     * Bakes a Stage to show.
     * @param fxmlPath a URL ot the new view.
     * @param title a title for a new scene.
     * @param layerIndex a layer of a stage to be shown on
     * @return
     */
    private Stage bakeStage(URL fxmlPath, String title, int layerIndex, Optional<Boolean> isResizable){
        Stage stage = null;
        FXMLLoader loader = null;
        Parent parent = null;
        Scene scene = null;
        loader = new FXMLLoader(fxmlPath);
        loader.setResources(ResourceBundle.getBundle("locale/lang", Locale.getDefault()));
        try {
            parent = loader.load();
            scene = new Scene(parent);
            scene.getStylesheets().add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.SELECTED_THEME));
            stage = new Stage();
            stage.setResizable(isResizable.isPresent() ? isResizable.get() : false);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setOnHiding(windowEvent -> System.out.println("A stage on layer " + layerIndex + " was resetted"));
            final Stage stageDuplicate = stage;
            stageDuplicate.setOnCloseRequest(windowEvent -> StageManager.this.closeStage(stageDuplicate));
            return stageDuplicate;
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Adds a stage. If one already exists, closes it.
     * @param stage a stage to be added.
     * @param viewURL stage's url
     * @param layerNumber number of a layer, to bind a stage to.
     * @return
     */
    private void addStage(Stage stage, URL viewURL,  int layerNumber) {
        if (stage != null) {
            if (stages.get(layerNumber) != null) {
                stages.get(layerNumber).close();
            }
            stages.put(layerNumber, stage);
            stagePaths.put(layerNumber, viewURL);
        }
    }

    public Stage getStage(int layer){
        return stages.get(layer);
    }

    /**
     * Shuts the application down
     */
    public void Shutdown(){
        for (int i = 0; i < MAX_LAYERS; i ++){
            if (stages.get(i) != null){
                stages.get(i).close();
            }
        }
    }

    /**
     * Closes a stage and all the stages on the upper levels from current.
     * @param stageToClose a stage to be closed.
     */
    public void closeStage(Stage stageToClose){
        int foundStageIndex = MAX_LAYERS;
        for (int i = 0 ; i < MAX_LAYERS; i++){
            if (stages.get(i) != null){
                if (stages.get(i) == stageToClose){
                    stages.get(i).close();
                    stages.put(i, null);
                    stagePaths.put(i, null);
                    foundStageIndex = i;
                    break;
                }
            }
        }
        for (int i = foundStageIndex + 1; i < MAX_LAYERS; i ++){
            if (stages.get(i) != null) {
                stages.get(i).close();
                stages.put(i, null);
                stagePaths.put(i, null);
            }
        }
    }
}

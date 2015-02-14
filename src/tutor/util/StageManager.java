package tutor.util;

import com.sun.istack.internal.NotNull;
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
    private int maxLayer;                   //max layer
    private static StageManager instance;

    private StageManager(int layersCount){
        maxLayer = layersCount;
        stagePaths = new LinkedHashMap<>(layersCount);
        stages = new LinkedHashMap<>(layersCount);
    }

    public static StageManager getInstance(int layersCount) {
        if (instance == null) {
            synchronized (StageManager.class) {
                if (instance == null)
                    instance = new StageManager(layersCount);
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
    public void navigateTo(@NotNull URL fxmlViewURL, @NotNull String title, @NotNull int layerIndex, Optional<Boolean> isResizable) {
        try {
            Stage stage = null;
            if (layerIndex > maxLayer || layerIndex < 0)
                return;
            if (stages.get(layerIndex) == null || stagePaths.get(layerIndex) == null) {
                addStage(bakeStage(fxmlViewURL, title, layerIndex, isResizable), fxmlViewURL, layerIndex);
            }
            stage = stages.get(layerIndex);
            String mainFMXLViewPath = stagePaths.get(layerIndex).toExternalForm();
            if (mainFMXLViewPath.equals(fxmlViewURL.toExternalForm())) {
                if (stage.isShowing())
                    return;
                stage.show();
            } else {
                stage.close();
                addStage(bakeStage(fxmlViewURL, title, layerIndex, isResizable), fxmlViewURL, layerIndex);
                stage = stages.get(layerIndex);
                stage.show();
            }
            stages.put(layerIndex, stage);
            stagePaths.put(layerIndex, fxmlViewURL);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    public void putStage(URL stagePath,Stage stage, int layer){
        if (layer > 0 && layer < maxLayer) {
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
        loader = new FXMLLoader(fxmlPath); //changing main stage
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

    /**
     * Shuts the application down
     */
    public void Shutdown(){
        for (int i = 0; i < maxLayer; i ++){
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
        int foundStageIndex = maxLayer;
        for (int i = 0 ; i < maxLayer; i++){
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
        for (int i = foundStageIndex + 1; i < maxLayer; i ++){
            if (stages.get(i) != null) {
                stages.get(i).close();
                stages.put(i, null);
                stagePaths.put(i, null);
            }
        }
    }
}
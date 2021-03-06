package tutor.util;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by user on 08.02.2015.
 * Operates the amount of layers of Stages, that can be activated and shown at once.
 */
public class StageManager {


    private Map<Integer,URL> stagePaths;      //Integer = stage layer, URL = stage's fxml view path
    private Map<Integer,Stage> stages;
    private Map<Integer, Initializable> controllers;
    private static final int MAX_LAYERS = 3;                   //max layer count
    private static StageManager instance;

    private StageManager(){
        stagePaths = new LinkedHashMap<>(MAX_LAYERS);
        stages = new LinkedHashMap<>(MAX_LAYERS);
        controllers = new LinkedHashMap<>(MAX_LAYERS);
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
     * @param isResizable shows whether a user should be able to resize the stage.
     * @param waitUntilCloses stands for whether you'd like to wait until this stage is closed to proceed to other stages or to give user the opportunity to freely move between stages.
     */
    public void navigateTo(URL fxmlViewURL, String title, int layerIndex, Optional<Boolean> isResizable, boolean waitUntilCloses) {
        try {

            if (layerIndex > MAX_LAYERS || layerIndex < 0)
                return;

            Stage stage = stages.get(layerIndex);
            URL stagePath = stagePaths.get(layerIndex);

            if (stage == null || stagePath == null) {
                addStage(bakeStage(fxmlViewURL, title, layerIndex, isResizable), fxmlViewURL, layerIndex);
                stage = stages.get(layerIndex);
                stagePath = stagePaths.get(layerIndex);
            }

            String mainFMXLViewPath = stagePath.toExternalForm();
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

    /**
     * Bakes a Stage to show.
     * @param fxmlPath a URL ot the new view.
     * @param title a title for a new scene.
     * @param layerIndex a layer of a stage to be shown on
     * @return the requested stage.
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
            controllers.put(layerIndex, (Initializable)loader.getController());
            scene = new Scene(parent);
            scene.getStylesheets().add(UserConfigHelper.getInstance().getParameter(UserConfigHelper.SELECTED_THEME));
            stage = new Stage();
            stage.setResizable(isResizable.isPresent() ? isResizable.get() : false);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setOnHiding(windowEvent -> System.out.println("A stage on layer " + layerIndex + " was resetted"));
            final Stage stageDuplicate = stage;
            if (layerIndex != 0)
                stageDuplicate.setOnCloseRequest(windowEvent -> StageManager.this.closeStage(stageDuplicate));
            else
                stageDuplicate.setOnCloseRequest(windowEvent -> Shutdown());
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
     * Returns a stage, currently displayed on a given layer.
     * @param layer shows the layer of the stage to get.
     * @return stage on a given layer.
     */
    public Stage getStage(int layer){
        return stages.get(layer);
    }

    public Stage getStage(Initializable controller){
        return stages.get(IntStream.range(0, controllers.size()).filter(index -> controllers.get(index) == controller).findFirst().getAsInt());
    }

    /**
     * Returns stage's controller.
     * @param layer layer, on which the view is displayed.
     * @return controller for the selected view
     */
    public Initializable getControllerForViewOnLayer(int layer){
        return controllers.get(layer);
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
        DbManager.getInstance().shutdown();
        Voice.getInstance().dispose();
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
                }
                if (foundStageIndex != MAX_LAYERS  && i > foundStageIndex) {
                    stages.get(i).close();
                    stages.put(i, null);
                    stagePaths.put(i, null);
                }
            }
        }
    }
}

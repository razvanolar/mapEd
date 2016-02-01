package mapEditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mapEditor.application.app_utils.constants.CssConstants;

/**
 *
 * Created by razvanolar on 28.12.2015.
 */
public class MapEditorView extends Application {

  private Scene scene;

  @Override
  public void init() {
    BorderPane mainContainer = new BorderPane();
    SplitPane centerSplitPane = new SplitPane();
    mainContainer.setCenter(centerSplitPane);
    scene = new Scene(mainContainer, 700, 400);
    String cssPath = CssConstants.getDefaultTheme();
    if (cssPath != null)
      scene.getStylesheets().add(cssPath);
    MapEditorController.getInstance().setSceneAndMainContainer(scene, mainContainer, centerSplitPane);
    MapEditorController.getInstance().initView();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.setTitle("MapEditor 1.1v");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

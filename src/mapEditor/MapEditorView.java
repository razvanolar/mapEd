package mapEditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.repo.RepoController;

/**
 *
 * Created by razvanolar on 28.12.2015.
 */
public class MapEditorView extends Application {

  private Stage primaryStage;
  private Stage createProjectStage;
  private Scene primaryScene;
  private Scene createProjectScene;

  @Override
  public void init() {
    if (true)
      initPrimaryStageElements();
    else
      initCreateProjectStage();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    if (true) {
      primaryStage.setScene(primaryScene);
      primaryStage.setMaximized(true);
      primaryStage.setTitle("MapEditor 1.1v");
      primaryStage.show();
    } else {
      createProjectStage = new Stage(StageStyle.DECORATED);
      createProjectStage.setScene(createProjectScene);
      createProjectStage.show();
    }
  }

  private void initPrimaryStageElements() {
    BorderPane mainContainer = new BorderPane();
    SplitPane centerSplitPane = new SplitPane();
    mainContainer.setCenter(centerSplitPane);
    primaryScene = new Scene(mainContainer, 700, 400);
    String cssPath = CssConstants.getDefaultTheme();
    if (cssPath != null)
      primaryScene.getStylesheets().add(cssPath);
    MapEditorController.getInstance().setSceneAndMainContainer(primaryScene, mainContainer, centerSplitPane);
    MapEditorController.getInstance().initView();
  }

  private void initCreateProjectStage() {
    BorderPane mainContainer = new BorderPane();
    createProjectScene = new Scene(mainContainer, 600, 400);
  }

  public static void main(String[] args) {
    RepoController repoController = new RepoController();
    launch(args);
  }
}

package mapEditor;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mapEditor.application.create_project_part.CreateProjectController;
import mapEditor.application.create_project_part.CreateProjectView;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.repo.RepoController;
import mapEditor.application.repo.SystemParameters;

/**
 *
 * Created by razvanolar on 28.12.2015.
 */
public class MapEditorView extends Application {

  private Stage primaryStage;
  private Stage createProjectStage;
  private Scene primaryScene;
  private Scene createProjectScene;
  private Image logo;

  @Override
  public void init() {
    MapEditorController.getInstance().setMapEditorView(this);
    logo = ImageProvider.logo();
    if (false)
      initPrimaryStageElements();
    else
      initCreateProjectStage();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    if (false) {
      showPrimaryStage();
    } else {
      showCreateProjectStage();
    }
  }

  public void showPrimaryStage() {
    if (primaryScene == null)
      initPrimaryStageElements();
    this.primaryStage.setScene(primaryScene);
    this.primaryStage.setMaximized(true);
    this.primaryStage.setTitle("MapEditor 1.1v");
    this.primaryStage.getIcons().add(logo);
    this.primaryStage.show();
  }

  public void showCreateProjectStage() {
    createProjectStage = new Stage(StageStyle.DECORATED);
    createProjectStage.setScene(createProjectScene);
    createProjectStage.setResizable(false);
    createProjectStage.getIcons().add(logo);
    createProjectStage.show();
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
    MapEditorController.getInstance().initPrimaryView();
  }

  private void initCreateProjectStage() {
    CreateProjectController.ICreateProjectView createProjectView = new CreateProjectView();
    BorderPane mainContainer = new BorderPane(createProjectView.asNode());
    createProjectScene = new Scene(mainContainer, 700, 400);

    String cssPath = CssConstants.getDefaultTheme();
    if (cssPath != null)
      createProjectScene.getStylesheets().add(cssPath);

    Image logo = ImageProvider.logoText60();
    HBox headerPane = new HBox(new ImageView(logo));
    headerPane.setPadding(new Insets(5));
    headerPane.setPrefHeight(60);
    mainContainer.setTop(headerPane);
    MapEditorController.getInstance().initCreateProjectView(createProjectView);

    createProjectView.getCancelProjectButton().setOnAction(event -> {
      if (createProjectStage != null)
        createProjectStage.close();
    });
  }

  public void clearCreateProjectView() {
    if (createProjectStage != null)
      createProjectStage.close();
    createProjectStage = null;
    createProjectScene = null;
  }

  public static void main(String[] args) {
    try {
      RepoController repoController = new RepoController();
      SystemParameters.PROJECTS = repoController.loadExistingProjects();

      MapEditorController.getInstance().setRepoController(repoController);
      launch(args);
    } catch (Exception ex) {
      System.out.println("*** Unable to open the app. Exception message : " + ex.getMessage());
    }
  }
}

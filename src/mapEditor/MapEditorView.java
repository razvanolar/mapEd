package mapEditor;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mapEditor.application.create_project_part.CreateProjectController;
import mapEditor.application.create_project_part.CreateProjectView;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.constants.AppConstants;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.threads.MessageListener;
import mapEditor.application.repo.RepoController;
import mapEditor.application.repo.SystemParameters;
import mapEditor.application.repo.models.LWProjectModel;
import mapEditor.application.repo.models.ProjectModel;
import mapEditor.application.repo.types.ProjectStatus;

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

  private StackPane stackPane;
  private BorderPane mask;

  @Override
  public void init() {
    MapEditorController.getInstance().setMapEditorView(this);
    logo = ImageProvider.logo();
    mask = new BorderPane();
    mask.setBackground(new Background(new BackgroundFill(new Color(.4, .4, .4, .5), null, null)));
    if (AppParameters.CURRENT_PROJECT != null)
      initPrimaryStageElements();
    else
      initCreateProjectStage();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    if (AppParameters.CURRENT_PROJECT != null) {
      showPrimaryStage();
    } else {
      showCreateProjectStage();
    }
  }

  @Override
  public void stop() throws Exception {
    MapEditorController.getInstance().saveCurrentProjectState();

    for (Thread thread : SystemParameters.watchers) {
      thread.interrupt();
      System.out.println("> Thread " + thread.getName() + " stopped.");
    }

    if (SystemParameters.appListenerThread != null)
      SystemParameters.appListenerThread.interrupt();
  }

  public void showPrimaryStage() {
    if (primaryScene == null)
      initPrimaryStageElements();
    this.primaryStage.setScene(primaryScene);
    this.primaryStage.setMaximized(true);
    this.primaryStage.setTitle(AppParameters.CURRENT_PROJECT.getName() + " - " + AppParameters.CURRENT_PROJECT.getHomePath() + " - MapEditor 1.1v");
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
    stackPane = new StackPane(mainContainer);
    primaryScene = new Scene(stackPane, 700, 400);
    centerSplitPane.prefWidthProperty().bind(primaryScene.widthProperty());
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

  public void mask() {
    stackPane.getChildren().add(mask);
  }

  public void unmask() {
    if (stackPane.getChildren().contains(mask))
      stackPane.getChildren().remove(mask);
  }

  public void clearCreateProjectView() {
    if (createProjectStage != null)
      createProjectStage.close();
    createProjectStage = null;
    createProjectScene = null;
  }

  public void close() {
    if (createProjectStage != null)
      createProjectStage.close();
    if (primaryStage != null)
      primaryStage.close();
  }

  public static void main(String[] args) {
    try {
      RepoController repoController = new RepoController();
      SystemParameters.PROJECTS = repoController.loadExistingProjects();
      for (LWProjectModel model : SystemParameters.PROJECTS)
        if (model.getStatus() == ProjectStatus.OPENED) {
          ProjectModel project = repoController.loadProject(model, false);
          if (project != null) {
            AppParameters.CURRENT_PROJECT = project;
            break;
          }
        }

      MapEditorController.getInstance().setRepoController(repoController);
      SystemParameters.appListenerThread = new Thread(new MessageListener(), AppConstants.APP_LISTENER_THREAD_NAME);
      SystemParameters.appListenerThread.start();
      launch(args);
    } catch (Exception ex) {
      System.out.println("*** Unable to open the app. Exception message : " + ex.getMessage());
    }
  }
}

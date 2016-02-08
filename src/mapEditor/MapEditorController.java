package mapEditor;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mapEditor.application.create_project_part.CreateProjectController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.main_app_toolbars.project_tree_toolbar.ProjectVerticalToolbarController;
import mapEditor.application.main_part.main_app_toolbars.project_tree_toolbar.ProjectVerticalToolbarView;
import mapEditor.application.main_part.manage_images.ManageImagesController;
import mapEditor.application.main_part.manage_images.ManageImagesView;
import mapEditor.application.main_part.manage_maps.ManageMapsController;
import mapEditor.application.main_part.manage_maps.ManageMapsView;
import mapEditor.application.main_part.main_app_toolbars.main_toolbar.MapEditorToolbarController;
import mapEditor.application.main_part.main_app_toolbars.main_toolbar.MapEditorToolbarView;
import mapEditor.application.main_part.menu_bar.MapEditorMenuBarController;
import mapEditor.application.main_part.menu_bar.MapEditorMenuBarView;
import mapEditor.application.repo.RepoController;
import mapEditor.application.repo.models.ProjectModel;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class MapEditorController {

  private static MapEditorController INSTANCE;

  private MapEditorView mapEditorView;
  private Scene scene;
  private BorderPane mainContainer;
  private SplitPane centerSplitPane;

  private RepoController repoController;
  private MapEditorToolbarController toolbarController;
  private ManageMapsController manageMapsController;
  private ManageImagesController manageImagesController;

  public static MapEditorController getInstance() {
    if (INSTANCE == null)
      INSTANCE = new MapEditorController();
    return INSTANCE;
  }

  public void initPrimaryView() {
    /* init menu bar */
    MapEditorMenuBarController.IMapEditorMenuBarView menuBarView = new MapEditorMenuBarView();
    MapEditorMenuBarController menuBarController = new MapEditorMenuBarController(menuBarView);
    menuBarController.bind();

    /* init main toolbar */
    MapEditorToolbarController.IMapEditorToolbarView toolbarView = new MapEditorToolbarView();
    toolbarController = new MapEditorToolbarController(toolbarView);
    toolbarController.bind();

    addMapEditorMenuBarAndToolbar(menuBarView.asNode(), toolbarView.asNode());

    /* init left side toolbar : project toolbar */
    ProjectVerticalToolbarController.IProjectVerticalToolbarView projectVerticalToolbarView = new ProjectVerticalToolbarView();
    ProjectVerticalToolbarController projectVerticalToolbarController = new ProjectVerticalToolbarController(projectVerticalToolbarView);
    projectVerticalToolbarController.bind();
    mainContainer.setLeft(projectVerticalToolbarView.asNode());

    // use it when construct the project tree view; now it's only for testing
    setProjectTreeView();

    changeView();
  }

  public void initCreateProjectView(CreateProjectController.ICreateProjectView createProjectView) {
    CreateProjectController createProjectController = new CreateProjectController(createProjectView);
    createProjectController.bind();
  }

  /**
   * Change primary content view. It is switching between map view and image view.
   * It is called when the view selection of the toolbar is changed.
   */
  public void changeView() {
    if (toolbarController.isMapViewSelected())
      setMapView();
    else
      setImageView();
  }

  private void setMapView() {
    if (manageMapsController == null) {
      ManageMapsController.IMangeMapsView mapContentView = new ManageMapsView();
      manageMapsController = new ManageMapsController(mapContentView);
      manageMapsController.bind();
    }
    setContentView(manageMapsController.getView().asNode());
  }

  private void setImageView() {
    if (manageImagesController == null) {
      ManageImagesController.IManageImagesView imagesView = new ManageImagesView();
      manageImagesController = new ManageImagesController(imagesView);
      manageImagesController.bind();
    }
    setContentView(manageImagesController.getView().asNode());
  }

  private void setProjectTreeView() {
    StackPane stackPane = new StackPane();
    SplitPane.setResizableWithParent(stackPane, false);
    if (!centerSplitPane.getItems().isEmpty()) {
      centerSplitPane.getItems().remove(0);
      centerSplitPane.getItems().add(0, stackPane);
    } else
      centerSplitPane.getItems().add(stackPane);
    centerSplitPane.setDividerPositions(0.0);
  }

  private void setContentView(Region contentView) {
    double[] dividerPositions = centerSplitPane.getDividerPositions();
    if (centerSplitPane.getItems().size() == 2) {
      centerSplitPane.getItems().remove(1);
      centerSplitPane.getItems().add(contentView);
    } else {
      centerSplitPane.getItems().add(contentView);
    }

    if (dividerPositions.length > 0)
      centerSplitPane.setDividerPositions(dividerPositions);
  }

  private void addMapEditorMenuBarAndToolbar(Region menuBar, Region toolbar) {
    VBox pane = new VBox();
    pane.getChildren().addAll(menuBar, toolbar);
    mainContainer.setTop(pane);
  }

  /**
   * Load the project.
   * @param project - project
   * @param loadFromCreateProjectView true if the call it's made from CreateProjectView
   */
  public void loadProject(ProjectModel project, boolean loadFromCreateProjectView) {
    AppParameters.CURRENT_PROJECT = project;

    mapEditorView.showPrimaryStage();

    if (loadFromCreateProjectView)
      mapEditorView.clearCreateProjectView();
  }

  public void closeApp() {
    mapEditorView.close();
  }



  /* getters and setters */

  public void setSceneCursor(Cursor cursor) {
    scene.setCursor(cursor);
  }

  public void setSceneAndMainContainer(Scene scene, BorderPane mainContainer, SplitPane centerSplitPane) {
    this.scene = scene;
    this.mainContainer = mainContainer;
    this.centerSplitPane = centerSplitPane;
  }

  public void setMapEditorView(MapEditorView mapEditorView) {
    this.mapEditorView = mapEditorView;
  }

  public void setRepoController(RepoController repoController) {
    this.repoController = repoController;
  }

  public RepoController getRepoController() {
    return repoController;
  }

  public final Scene getScene() {
    return scene;
  }
}

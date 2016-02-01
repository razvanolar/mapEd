package mapEditor;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import mapEditor.application.main_app_toolbars.project_tree_toolbar.ProjectVerticalToolbarController;
import mapEditor.application.main_app_toolbars.project_tree_toolbar.ProjectVerticalToolbarView;
import mapEditor.application.manage_images.ManageImagesController;
import mapEditor.application.manage_images.ManageImagesView;
import mapEditor.application.manage_maps.ManageMapsController;
import mapEditor.application.manage_maps.ManageMapsView;
import mapEditor.application.main_app_toolbars.main_toolbar.MapEditorToolbarController;
import mapEditor.application.main_app_toolbars.main_toolbar.MapEditorToolbarView;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class MapEditorController {

  private static MapEditorController INSTANCE;
  private Scene scene;
  private BorderPane mainContainer;
  private SplitPane centerSplitPane;
  private MapEditorToolbarController toolbarController;
  private ManageMapsController manageMapsController;
  private ManageImagesController manageImagesController;

  public static MapEditorController getInstance() {
    if (INSTANCE == null)
      INSTANCE = new MapEditorController();
    return INSTANCE;
  }

  public void initView() {
    /* init main toolbar */
    MapEditorToolbarController.IMapEditorToolbarView toolbarView = new MapEditorToolbarView();
    toolbarController = new MapEditorToolbarController(toolbarView);
    toolbarController.bind();
    mainContainer.setTop(toolbarView.asNode());

    /* init left side toolbar : project toolbar */
    ProjectVerticalToolbarController.IProjectVerticalToolbarView projectVerticalToolbarView = new ProjectVerticalToolbarView();
    ProjectVerticalToolbarController projectVerticalToolbarController = new ProjectVerticalToolbarController(projectVerticalToolbarView);
    projectVerticalToolbarController.bind();
    mainContainer.setLeft(projectVerticalToolbarView.asNode());

    // use it when construct the project tree view; no it's only for testing
    setProjectTreeView();

    changeView();
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

  public void setSceneCursor(Cursor cursor) {
    scene.setCursor(cursor);
  }

  public void setSceneAndMainContainer(Scene scene, BorderPane mainContainer, SplitPane centerSplitPane) {
    this.scene = scene;
    this.mainContainer = mainContainer;
    this.centerSplitPane = centerSplitPane;
  }

  public final Scene getScene() {
    return scene;
  }
}

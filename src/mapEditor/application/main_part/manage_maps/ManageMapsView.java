package mapEditor.application.main_part.manage_maps;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.manage_maps.manage_layers.LayersController;
import mapEditor.application.main_part.manage_maps.manage_layers.LayersView;
import mapEditor.application.main_part.manage_maps.manage_tiles.ManageTilesController;
import mapEditor.application.main_part.manage_maps.manage_tiles.ManageTilesView;
import mapEditor.application.main_part.manage_maps.primary_map.PrimaryMapView;
import mapEditor.application.main_part.manage_maps.utils.MapContentStateKeys;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class ManageMapsView implements ManageMapsController.IMangeMapsView {

  private SplitPane splitPane;
  /* left */
  private TabPane mapsTabPane;
  /* right */
  private TabPane layersAndMinimapTabPane;
  private LayersController.ILayersView layersView;
  private ManageTilesController.IManageTilesView manageTilesView;

  public ManageMapsView() {
    initGUI();
  }

  private void initGUI() {
    mapsTabPane = new TabPane();
    layersView = new LayersView();
    layersAndMinimapTabPane = new TabPane(new Tab("Layers", layersView.asNode()));
    manageTilesView = new ManageTilesView();

    SplitPane rightSplitPane = new SplitPane(layersAndMinimapTabPane, manageTilesView.asNode());
    rightSplitPane.setOrientation(Orientation.VERTICAL);
    rightSplitPane.setDividerPositions(0.5);

    splitPane = new SplitPane(mapsTabPane, rightSplitPane);
    splitPane.setOrientation(Orientation.HORIZONTAL);
    splitPane.setDividerPositions(0.3);
    splitPane.prefWidthProperty().bind(MapEditorController.getInstance().getScene().widthProperty());
    splitPane.prefHeightProperty().bind(MapEditorController.getInstance().getScene().heightProperty());
    SplitPane.setResizableWithParent(rightSplitPane, false);
  }

  @Override
  public ScrollPane addMap(String title, PrimaryMapView mapView, boolean selectTab) {
    ScrollPane scrollPane = new ScrollPane(mapView);
//    BorderPane borderPane = new BorderPane(scrollPane);

    scrollPane.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    mapView.widthProperty().bind(scrollPane.widthProperty());
    mapView.heightProperty().bind(scrollPane.heightProperty());

    Tab tab = new Tab(title, scrollPane);
    tab.setUserData(mapView);
    tab.getProperties().put(MapContentStateKeys.MAP_SCROLL_PANE, scrollPane);
    mapsTabPane.getTabs().add(tab);
    if (selectTab)
      mapsTabPane.getSelectionModel().select(tab);
    return scrollPane;
  }

  public TabPane getMapsTabPane() {
    return mapsTabPane;
  }

  public LayersController.ILayersView getLayersView() {
    return layersView;
  }

  public ManageTilesController.IManageTilesView getManageTilesView() {
    return manageTilesView;
  }

  public double getDividerPosition() {
    double[] dividerPositions = splitPane.getDividerPositions();
    if (dividerPositions != null && dividerPositions.length > 0)
      return dividerPositions[0];
    return -1;
  }

  public void setDividerPosition(double value) {
    splitPane.setDividerPositions(value);
  }

  public Region asNode() {
    return splitPane;
  }
}

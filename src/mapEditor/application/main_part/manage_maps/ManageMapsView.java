package mapEditor.application.main_part.manage_maps;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.manage_tiles.ManageTilesController;
import mapEditor.application.main_part.manage_tiles.ManageTilesView;
import mapEditor.application.main_part.manage_maps.primary_map.PrimaryMapView;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class ManageMapsView implements ManageMapsController.IMangeMapsView {

  private SplitPane splitPane;
  /* left */
  private PrimaryMapView primaryMapView;
  private ScrollPane canvasScrollPane;
  /* right */
  private ManageTilesController.IManageTilesView manageTilesView;

  public ManageMapsView() {
    initGUI();
  }

  private void initGUI() {
    primaryMapView = new PrimaryMapView();
    canvasScrollPane = new ScrollPane(primaryMapView);
    manageTilesView = new ManageTilesView();

    SplitPane rightSplitPane = new SplitPane(new StackPane(), manageTilesView.asNode());
    rightSplitPane.setOrientation(Orientation.VERTICAL);
    rightSplitPane.setDividerPositions(0.5);

    canvasScrollPane.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);

    splitPane = new SplitPane(canvasScrollPane, rightSplitPane);
    splitPane.setOrientation(Orientation.HORIZONTAL);
    splitPane.setDividerPositions(0.4);
    splitPane.prefWidthProperty().bind(MapEditorController.getInstance().getScene().widthProperty());
    splitPane.prefHeightProperty().bind(MapEditorController.getInstance().getScene().heightProperty());
    SplitPane.setResizableWithParent(rightSplitPane, false);
  }

  public PrimaryMapView getPrimaryMapView() {
    return primaryMapView;
  }

  public ScrollPane getCanvasScrollPane() {
    return canvasScrollPane;
  }

  public ManageTilesController.IManageTilesView getManageTilesView() {
    return manageTilesView;
  }

  public Region asNode() {
    return splitPane;
  }
}

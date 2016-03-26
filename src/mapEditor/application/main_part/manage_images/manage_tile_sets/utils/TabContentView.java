package mapEditor.application.main_part.manage_images.manage_tile_sets.utils;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.views.others.HorizontalSeparatorBar;
import mapEditor.application.main_part.manage_images.manage_tile_sets.cropped_tiles.CroppedTilesPathView;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public class TabContentView implements View {

  private SplitPane splitPane;
  private ScrollPane canvasContainer;
  private BorderPane borderPane;
  private BorderPane croppedTilesContainer;
  private VBox tilesPane;

  private Canvas canvas;
  private CroppedTilesPathView pathView;
  private boolean simpleView;

  public TabContentView(Canvas canvas, CroppedTilesPathView pathView) {
    this.canvas = canvas;
    this.pathView = pathView;
    initGUI();
  }

  private void initGUI() {
    canvasContainer = new ScrollPane(canvas);
    borderPane = new BorderPane(canvasContainer);
    tilesPane = new VBox(7);
    ScrollPane scrollPane = new ScrollPane(tilesPane);
    croppedTilesContainer = new BorderPane(scrollPane);
    splitPane = new SplitPane(borderPane, croppedTilesContainer);

    splitPane.setOrientation(Orientation.VERTICAL);
    splitPane.setDividerPositions(0.8);
    SplitPane.setResizableWithParent(borderPane, false);

    canvasContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    canvasContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    canvasContainer.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);

    scrollPane.getStyleClass().add(CssConstants.TAB_CONTENT_VIEW_TILES_PANE);
    scrollPane.setMinHeight(0);
    scrollPane.setFitToWidth(true);
    tilesPane.setPadding(new Insets(5));
  }

  public void addDetailedTileForm(Region node) {
    ObservableList<Node> children = tilesPane.getChildren();
    if (children.isEmpty())
      children.addAll(pathView.asNode(), new HorizontalSeparatorBar(2));
    children.addAll(node, new HorizontalSeparatorBar(2));
  }

  public void setSimpleTileView(Region node) {
    tilesPane.getChildren().clear();
    tilesPane.getChildren().addAll(pathView.asNode(), node);
  }

  public int removeDetailedTileForm(Region node) {
    ObservableList<Node> children = tilesPane.getChildren();
    int index = children.indexOf(node);
    if (index != -1)
      children.remove(index + 1);
    children.remove(node);
    if (children.size() == 2)
      children.clear();
    return children.size();
  }

  public void clearTilesPane() {
    tilesPane.getChildren().clear();
  }

  public void setToolBar(ToolBar toolBar) {
    borderPane.setBottom(toolBar);
  }

  public void setVerticalToolBar(ToolBar verticalToolBar) {
    croppedTilesContainer.setLeft(verticalToolBar);
  }

  public ScrollPane getCanvasContainer() {
    return canvasContainer;
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public CroppedTilesPathView getPathView() {
    return pathView;
  }

  public boolean isEmptyTilesPane() {
    return tilesPane.getChildren().isEmpty();
  }

  public boolean isSimpleView() {
    return simpleView;
  }

  public void setSimpleView(boolean simpleView) {
    this.simpleView = simpleView;
  }

  @Override
  public Region asNode() {
    return splitPane;
  }
}

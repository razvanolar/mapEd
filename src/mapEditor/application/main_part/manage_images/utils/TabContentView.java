package mapEditor.application.main_part.manage_images.utils;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public class TabContentView implements View {

  private SplitPane splitPane;
  private ScrollPane canvasContainer;
  private BorderPane borderPane;
  private VBox tilesPane;

  private Canvas canvas;

  public TabContentView(Canvas canvas) {
    this.canvas = canvas;
    initGUI();
  }

  private void initGUI() {
    canvasContainer = new ScrollPane(canvas);
    borderPane = new BorderPane(canvasContainer);
    tilesPane = new VBox(7);
    ScrollPane scrollPane = new ScrollPane(tilesPane);
    splitPane = new SplitPane(borderPane, scrollPane);

    splitPane.setOrientation(Orientation.VERTICAL);
    splitPane.setDividerPositions(1);
    SplitPane.setResizableWithParent(borderPane, false);

    canvasContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    canvasContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    canvasContainer.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);

    scrollPane.getStyleClass().add(CssConstants.TAB_CONTENT_VIEW_TILES_PANE);
    scrollPane.setMinHeight(0);
    tilesPane.setPadding(new Insets(5));
    tilesPane.prefWidthProperty().bind(scrollPane.widthProperty());
  }

  public void addTileForm(Region node) {
    tilesPane.getChildren().add(node);
  }

  public void removeTileForm(Region node) {
    tilesPane.getChildren().remove(node);
  }

  public void setToolBar(ToolBar toolBar) {
    borderPane.setBottom(toolBar);
  }

  public ScrollPane getCanvasContainer() {
    return canvasContainer;
  }

  public Canvas getCanvas() {
    return canvas;
  }

  @Override
  public Region asNode() {
    return splitPane;
  }
}

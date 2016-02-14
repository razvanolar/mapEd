package mapEditor.application.main_part.manage_images.utils;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.manage_images.cropped_tiles.CroppedTilesPathView;
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
  private ToolBar verticalToolBar;
  private Button upButton;
  private Button downButton;
  private Button scrollDownButton;
  private Button clearButton;

  private Canvas canvas;
  private CroppedTilesPathView pathView;

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
    BorderPane croppedTilesContainer = new BorderPane(scrollPane);
    splitPane = new SplitPane(borderPane, croppedTilesContainer);
    upButton = new Button("Up");
    downButton = new Button("Down");
    scrollDownButton = new Button("Scroll");
    clearButton = new Button("Clear");
    verticalToolBar = new ToolBar(new Group(upButton), new Group(downButton), new Group(scrollDownButton), new Group(clearButton));

    upButton.setRotate(-90);
    downButton.setRotate(-90);
    scrollDownButton.setRotate(-90);
    clearButton.setRotate(-90);

    verticalToolBar.setOrientation(Orientation.VERTICAL);
    croppedTilesContainer.setLeft(verticalToolBar);

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
    ObservableList<Node> children = tilesPane.getChildren();
    if (children.isEmpty())
      children.add(pathView.asNode());
    children.add(node);
  }

  public int removeTileForm(Region node) {
    ObservableList<Node> children = tilesPane.getChildren();
    children.remove(node);
    if (children.size() == 1)
      children.clear();
    return children.size();
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

package mapEditor.application.main_part.manage_images.utils;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.views.others.HorizontalSeparatorBar;
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
  private ToggleButton simpleViewButton;

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
    simpleViewButton = new ToggleButton("Simple");
    verticalToolBar = new ToolBar(new Group(upButton),
            new Group(downButton),
            new Group(scrollDownButton),
            new Group(clearButton),
            new Group(simpleViewButton));

    upButton.setRotate(-90);
    downButton.setRotate(-90);
    scrollDownButton.setRotate(-90);
    clearButton.setRotate(-90);
    simpleViewButton.setRotate(-90);

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

    //TODO: set the button based on a parameter from user preferences
    simpleViewButton.setSelected(true);
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

  public ScrollPane getCanvasContainer() {
    return canvasContainer;
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public ToggleButton getSimpleViewButton() {
    return simpleViewButton;
  }

  public boolean isSimpleView() {
    return simpleViewButton.isSelected();
  }

  @Override
  public Region asNode() {
    return splitPane;
  }
}

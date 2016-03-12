package mapEditor.application.main_part.manage_images.manage_tiles;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.views.others.FillToolItem;

/**
 *
 * Created by razvanolar on 11.03.2016.
 */
public class ManageEditEditTilesView implements ManageEditTilesController.IManageEditTilesView {

  private BorderPane mainContainer;
  private FlowPane tilesFlowPane;

  private Slider imageHueSlider;
  private Slider imageBrightnessSlider;
  private Slider imageContrastSlider;
  private Slider imageSaturationSlider;
  private TextField nameTextField;
  private TextField pathTextField;
  private TextField allPathTextField;
  private Button pathButton;
  private Button loadTilesButton;
  private Button allPathButton;
  private CheckBox usePathForAllCheckBox;

  public ManageEditEditTilesView() {
    initGUI();
  }

  private void initGUI() {
    imageHueSlider = new Slider(-1, 1, 0);
    imageBrightnessSlider = new Slider(-1, 1, 0);
    imageContrastSlider = new Slider(-1, 1, 0);
    imageSaturationSlider = new Slider(-1, 1, 0);

    nameTextField = new TextField();
    pathTextField = new TextField();
    allPathTextField = new TextField();
    pathButton = new Button("...");
    allPathButton = new Button("...");
    loadTilesButton = new Button("Load");
    usePathForAllCheckBox = new CheckBox("Use For All");

    tilesFlowPane = new FlowPane(Orientation.HORIZONTAL, 5, 5);
    ScrollPane tilesScrollPane = new ScrollPane(tilesFlowPane);
    GridPane imageStylesGridPane = new GridPane();
    VBox imageSlidersContainer = new VBox(5, imageStylesGridPane);
    SplitPane splitContainer = new SplitPane(tilesScrollPane, imageSlidersContainer);
    ToolBar toolBar = new ToolBar(loadTilesButton, new FillToolItem(), new Text("Path : "), allPathTextField, allPathButton, usePathForAllCheckBox);
    mainContainer = new BorderPane(splitContainer);

    SplitPane.setResizableWithParent(imageSlidersContainer, false);
    splitContainer.setDividerPositions(0.5);
    allPathTextField.setMinWidth(250);

    imageStylesGridPane.setAlignment(Pos.TOP_CENTER);
    imageStylesGridPane.setHgap(5);
    imageStylesGridPane.setVgap(5);
    imageStylesGridPane.setPadding(new Insets(10));
    imageStylesGridPane.add(new Text("Hue : "), 0, 0);
    imageStylesGridPane.add(imageHueSlider, 1, 0, 2, 1);
    imageStylesGridPane.add(new Text("Brightness : "), 0, 1);
    imageStylesGridPane.add(imageBrightnessSlider, 1, 1, 2, 1);
    imageStylesGridPane.add(new Text("Contrast : "), 0, 2);
    imageStylesGridPane.add(imageContrastSlider, 1, 2, 2, 1);
    imageStylesGridPane.add(new Text("Saturation : "), 0, 3);
    imageStylesGridPane.add(imageSaturationSlider, 1, 3, 2, 1);
    imageStylesGridPane.add(new Text("Name : "), 0, 4);
    imageStylesGridPane.add(nameTextField, 1, 4, 2, 1);
    imageStylesGridPane.add(new Text("Path : "), 0, 5);
    imageStylesGridPane.add(pathTextField, 1, 5);
    imageStylesGridPane.add(pathButton, 2, 5);

    imageSlidersContainer.setMinWidth(250);

    tilesFlowPane.prefWidthProperty().bind(tilesScrollPane.widthProperty());
    tilesFlowPane.prefHeightProperty().bind(tilesScrollPane.heightProperty());

    tilesFlowPane.setAlignment(Pos.CENTER);
    tilesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    tilesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    tilesScrollPane.setPrefWidth(200);
    tilesScrollPane.setPrefHeight(170);
    tilesScrollPane.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);

    mainContainer.setTop(toolBar);
  }

  @Override
  public void addTile(Node imageView) {
    tilesFlowPane.getChildren().add(imageView);
  }

  public Slider getImageHueSlider() {
    return imageHueSlider;
  }

  public Slider getImageBrightnessSlider() {
    return imageBrightnessSlider;
  }

  public Slider getImageContrastSlider() {
    return imageContrastSlider;
  }

  public Slider getImageSaturationSlider() {
    return imageSaturationSlider;
  }

  public TextField getNameTextField() {
    return nameTextField;
  }

  public TextField getPathTextField() {
    return pathTextField;
  }

  public TextField getAllPathTextField() {
    return allPathTextField;
  }

  public Button getPathButton() {
    return pathButton;
  }

  public Button getLoadTilesButton() {
    return loadTilesButton;
  }

  public Button getAllPathButton() {
    return allPathButton;
  }

  public CheckBox getUsePathForAllCheckBox() {
    return usePathForAllCheckBox;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

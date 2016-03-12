package mapEditor.application.main_part.manage_images.manage_tiles;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.constants.CssConstants;

/**
 *
 * Created by razvanolar on 11.03.2016.
 */
public class ManageEditEditTilesView implements ManageEditTilesController.IManageEditTilesView {

  private SplitPane mainContainer;
  private FlowPane tilesFlowPane;

  private Slider imageHueSlider;
  private Slider imageBrightnessSlider;
  private Slider imageContrastSlider;
  private Slider imageSaturationSlider;
  private TextField nameTextField;
  private TextField pathTextField;
  private Button pathButton;
  private CheckBox overwriteCheckBox;

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
    pathButton = new Button("...");
    overwriteCheckBox = new CheckBox();

    tilesFlowPane = new FlowPane(Orientation.HORIZONTAL, 5, 5);
    ScrollPane tilesScrollPane = new ScrollPane(tilesFlowPane);
    GridPane imageStylesGridPane = new GridPane();
    VBox imageSlidersContainer = new VBox(5, imageStylesGridPane);
    mainContainer = new SplitPane(tilesScrollPane, imageSlidersContainer);

    SplitPane.setResizableWithParent(imageSlidersContainer, false);
    mainContainer.setDividerPositions(0.5);

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
    imageStylesGridPane.add(new Text("Overwrite : "), 0, 6);
    imageStylesGridPane.add(overwriteCheckBox, 1, 6);

    imageSlidersContainer.setMinWidth(250);

    tilesFlowPane.prefWidthProperty().bind(tilesScrollPane.widthProperty());
    tilesFlowPane.prefHeightProperty().bind(tilesScrollPane.heightProperty());

    tilesFlowPane.setAlignment(Pos.CENTER);
    tilesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    tilesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    tilesScrollPane.setPrefWidth(150);
    tilesScrollPane.setPrefHeight(150);
    tilesScrollPane.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);
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

  public Button getPathButton() {
    return pathButton;
  }

  public CheckBox getOverwriteCheckBox() {
    return overwriteCheckBox;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

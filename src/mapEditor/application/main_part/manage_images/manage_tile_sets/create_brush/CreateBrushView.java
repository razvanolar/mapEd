package mapEditor.application.main_part.manage_images.manage_tile_sets.create_brush;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * Created by razvanolar on 03.04.2016.
 */
public class CreateBrushView implements CreateBrushController.ICreateBrushView {

  private BorderPane mainContainer;
  private SplitPane splitPane;
  private VBox fieldsContainer;
  private Button addBrushButton;
  private TextField pathTextField;
  private Button pathButton;

  public CreateBrushView() {
    initGUI();
  }

  private void initGUI() {
    addBrushButton = new Button("Add New Brush");
    pathButton = new Button("...");
    pathTextField = new TextField();
    ToolBar toolBar = new ToolBar(addBrushButton, new Separator(), new Text("Path : "), pathTextField, pathButton);
    fieldsContainer = new VBox(5);
    splitPane = new SplitPane(fieldsContainer);
    mainContainer = new BorderPane(splitPane);

    pathTextField.setMinWidth(300);
    fieldsContainer.setPadding(new Insets(5));

    mainContainer.setTop(toolBar);
    mainContainer.setPrefWidth(850);
    mainContainer.setPrefHeight(600);
  }

  public void addCanvasContainer(Region region) {
    splitPane.getItems().add(region);
    splitPane.setDividerPositions(.3);
  }

  public Button getAddBrushButton() {
    return addBrushButton;
  }

  public void addBrushView(Region region) {
    fieldsContainer.getChildren().add(region);
  }

  public void removeBrushView(Region region) {
    fieldsContainer.getChildren().remove(region);
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

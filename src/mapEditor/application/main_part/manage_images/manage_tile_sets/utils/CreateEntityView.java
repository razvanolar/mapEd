package mapEditor.application.main_part.manage_images.manage_tile_sets.utils;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mapEditor.application.main_part.types.View;

/**
 * Used as an abstract window for the views from which we can crop parts of the tileset
 *
 * Created by razvanolar on 05.06.2016.
 */
public class CreateEntityView implements View {

  protected BorderPane mainContainer;
  protected SplitPane splitPane;
  protected VBox fieldsContainer;
  protected Button addButton;
  protected TextField pathTextField;
  protected Button pathButton;

  private String addButtonText;

  public CreateEntityView(String addButtonText) {
    this.addButtonText = addButtonText;
    initGUI();
  }

  private void initGUI() {
    addButton = new Button(addButtonText);
    pathButton = new Button("...");
    pathTextField = new TextField();
    ToolBar toolBar = new ToolBar(addButton, new Separator(), new Text("Path : "), pathTextField, pathButton);
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

  public Button getAddButton() {
    return addButton;
  }

  public TextField getPathTextField() {
    return pathTextField;
  }

  public Button getPathButton() {
    return pathButton;
  }

  public void addEntityView(Region region) {
    fieldsContainer.getChildren().add(region);
  }

  public void removeEntityView(Region region) {
    fieldsContainer.getChildren().remove(region);
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

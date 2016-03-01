package mapEditor.application.main_part.manage_maps.manage_layers;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mapEditor.application.main_part.app_utils.constants.CssConstants;

/**
 *
 * Created by razvanolar on 23.02.2016.
 */
public class LayersView implements LayersController.ILayersView {

  private BorderPane mainContainer;
  private VBox layersContainer;
  private Button addLayerButton;
  private Button editLayerButton;
  private Button deleteLayerButton;
  private Button moveLayerUpButton;
  private Button moveLayerDownButton;

  public LayersView() {
    initGUI();
  }

  private void initGUI() {
    addLayerButton = new Button("Add");
    editLayerButton = new Button("Edit");
    deleteLayerButton = new Button("Delete");
    moveLayerUpButton = new Button("Up");
    moveLayerDownButton = new Button("Down");
    layersContainer = new VBox();
    ScrollPane scrollPane = new ScrollPane(layersContainer);
    mainContainer = new BorderPane(scrollPane);
    ToolBar toolBar = new ToolBar(addLayerButton, editLayerButton, deleteLayerButton, moveLayerUpButton, moveLayerDownButton);

    scrollPane.setFitToWidth(true);

    layersContainer.setAlignment(Pos.TOP_CENTER);
    layersContainer.getStyleClass().add(CssConstants.LAYERS_VIEW);

    mainContainer.setBottom(toolBar);
  }

  @Override
  public void addLayer(Region layer) {
    layersContainer.getChildren().add(layer);
  }

  @Override
  public void removeLayer(Region layer) {
    layersContainer.getChildren().remove(layer);
  }

  @Override
  public void moveLayerUp(Region layer) {
    int index = getLayerIndex(layer);
    if (index <= 0)
      return;
    layersContainer.getChildren().remove(index);
    layersContainer.getChildren().add(index - 1, layer);
  }

  @Override
  public void moveLayerDown(Region layer) {
    int size = layersContainer.getChildren().size();
    if (size == 0)
      return;
    int index = getLayerIndex(layer);
    if (index == -1 || index == size - 1)
      return;
    layersContainer.getChildren().remove(index);
    layersContainer.getChildren().add(index + 1, layer);
  }

  @Override
  public void removeAllLayers() {
    layersContainer.getChildren().clear();
  }

  @Override
  public int getLayerIndex(Region layer) {
    return layersContainer.getChildren().indexOf(layer);
  }

  @Override
  public int getLayersNumber() {
    return layersContainer.getChildren().size();
  }

  public Button getAddLayerButton() {
    return addLayerButton;
  }

  public Button getEditLayerButton() {
    return editLayerButton;
  }

  public Button getDeleteLayerButton() {
    return deleteLayerButton;
  }

  public Button getMoveLayerUpButton() {
    return moveLayerUpButton;
  }

  public Button getMoveLayerDownButton() {
    return moveLayerDownButton;
  }

  public ObservableList<Node> getLayers() {
    return layersContainer.getChildren();
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

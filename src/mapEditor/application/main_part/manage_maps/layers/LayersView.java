package mapEditor.application.main_part.manage_maps.layers;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
    mainContainer = new BorderPane(layersContainer);
    ToolBar toolBar = new ToolBar(addLayerButton, editLayerButton, deleteLayerButton, moveLayerUpButton, moveLayerDownButton);

    layersContainer.setAlignment(Pos.TOP_CENTER);
    layersContainer.getStyleClass().add(CssConstants.LAYERS_VIEW);

    mainContainer.setBottom(toolBar);
  }

  @Override
  public void addLayer(Region layer) {
    layersContainer.getChildren().add(layer);
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

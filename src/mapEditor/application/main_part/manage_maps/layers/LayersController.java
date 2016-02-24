package mapEditor.application.main_part.manage_maps.layers;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.models.LayerModel;
import mapEditor.application.main_part.app_utils.models.LayerType;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.manage_maps.utils.SelectableLayerListener;
import mapEditor.application.main_part.manage_maps.utils.SelectableLayerView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 23.02.2016.
 */
public class LayersController implements Controller, SelectableLayerListener {

  public interface ILayersView extends View {
    void addLayer(Region layer);
    void removeLayer(Region layer);
    void moveLayerUp(Region layer);
    void moveLayerDown(Region layer);
    int getLayerIndex(Region layer);
    int getLayersNumber();
    Button getAddLayerButton();
    Button getEditLayerButton();
    Button getDeleteLayerButton();
    Button getMoveLayerUpButton();
    Button getMoveLayerDownButton();
    ObservableList<Node> getLayers();
  }

  private ILayersView view;
  private SelectableLayerView selectedLayer;

  public LayersController(ILayersView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    addTestLayers();
    addListeners();
  }

  private void addListeners() {
    view.getAddLayerButton().setOnAction(event -> onAddLayerButtonSelection());
    view.getEditLayerButton().setOnAction(event -> onEditLayerButtonSelection());
    view.getDeleteLayerButton().setOnAction(event -> onDeleteLayerButtonSelection());
    view.getMoveLayerUpButton().setOnAction(event -> onMoveLayerUpButtonSelection());
    view.getMoveLayerDownButton().setOnAction(event -> onMoveLayerDownButtonSelection());
  }

  /**
   * Called when adding a new layer
   */
  private void onAddLayerButtonSelection() {
    OkCancelDialog dialog = createAddEditDialog("Add Layer", null);
    dialog.getOkButton().setOnAction(event -> {
      CreateEditLayersController controller = (CreateEditLayersController) dialog.getController();
      view.addLayer(new SelectableLayerView(controller.getModel(), LayersController.this));
      dialog.close();
    });
    dialog.show();
  }

  /**
   * Called when editing selected layer
   */
  private void onEditLayerButtonSelection() {
    if (selectedLayer == null)
      return;
    OkCancelDialog dialog = createAddEditDialog("Edit Layer", selectedLayer.getLayerModel());
    dialog.getOkButton().setOnAction(event -> {
      CreateEditLayersController controller = (CreateEditLayersController) dialog.getController();
      selectedLayer.updateModel(controller.getModel());
      dialog.close();
    });
    dialog.show();
  }

  /**
   * Called when deleting selected layer
   */
  private void onDeleteLayerButtonSelection() {
    if (selectedLayer == null)
      return;
    view.removeLayer(selectedLayer);
    selectedLayer = null;
  }

  /**
   * Called when trying to move selected layer up.
   */
  private void onMoveLayerUpButtonSelection() {
    if (selectedLayer == null)
      return;
    int index = view.getLayerIndex(selectedLayer);
    if (index == -1 || index == 0)
      return;

    view.moveLayerUp(selectedLayer);
  }

  /**
   * Called when trying to move selected layer down.
   */
  private void onMoveLayerDownButtonSelection() {
    if (selectedLayer == null)
      return;
    int size = view.getLayersNumber();
    int index = view.getLayerIndex(selectedLayer);
    if (index == -1 || index == size - 1)
      return;

    view.moveLayerDown(selectedLayer);
  }

  private OkCancelDialog createAddEditDialog(String title, LayerModel model) {
    OkCancelDialog dialog = new OkCancelDialog(title, StageStyle.UTILITY, Modality.APPLICATION_MODAL, false);

    CreateEditLayersController.ICreateEditLayersView view = new CreateEditLayersView();
    CreateEditLayersController controller = new CreateEditLayersController(view, model, dialog.getOkButton(), getLayers());
    controller.bind();

    dialog.setContent(view.asNode());
    dialog.setController(controller);
    return dialog;
  }

  @Override
  public void selectedLayerChanged(SelectableLayerView selectedLayer) {
    if (this.selectedLayer == null)
      this.selectedLayer = selectedLayer;
    else if (this.selectedLayer != selectedLayer) {
      this.selectedLayer.unselect();
      this.selectedLayer = selectedLayer;
    }
  }

  public List<LayerModel> getLayers() {
    ObservableList<Node> children = view.getLayers();
    List<LayerModel> layers = new ArrayList<>();
    children.stream().filter(child -> child instanceof SelectableLayerView).forEach(child -> {
      SelectableLayerView selectableLayer = (SelectableLayerView) child;
      if (selectableLayer.getLayerModel() != null)
        layers.add(selectableLayer.getLayerModel());
    });
    return layers;
  }

  // TODO: delete
  private void addTestLayers() {
    view.addLayer(new SelectableLayerView(new LayerModel("background", LayerType.BACKGROUND), this));
    view.addLayer(new SelectableLayerView(new LayerModel("objects", LayerType.OBJECT), this));
    view.addLayer(new SelectableLayerView(new LayerModel("foreground", LayerType.FOREGROUND), this));
  }
}

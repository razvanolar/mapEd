package mapEditor.application.main_part.manage_maps.manage_layers;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.models.LayerModel;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.manage_maps.manage_layers.create_edit_layers.CreateEditLayersContextMenuController;
import mapEditor.application.main_part.manage_maps.manage_layers.create_edit_layers.CreateEditLayersContextMenuView;
import mapEditor.application.main_part.manage_maps.manage_layers.create_edit_layers.CreateEditLayersController;
import mapEditor.application.main_part.manage_maps.manage_layers.create_edit_layers.CreateEditLayersView;
import mapEditor.application.main_part.manage_maps.utils.listeners.MapLayersListener;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectableLayerListener;
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
    void removeAllLayers();
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
  private CreateEditLayersContextMenuController contextMenuController;
  private MapLayersListener listener;

  public LayersController(ILayersView view, MapLayersListener listener) {
    this.view = view;
    this.listener = listener;
  }

  @Override
  public void bind() {
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
      LayerModel layer = controller.getModel();
      view.addLayer(new SelectableLayerView(layer, LayersController.this));
      dialog.close();
      listener.addLayer(layer);
    });
    dialog.show();
  }

  /**
   * Called when editing selected layer
   */
  public void onEditLayerButtonSelection() {
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
  public void onDeleteLayerButtonSelection() {
    if (selectedLayer == null)
      return;
    LayerModel layer = selectedLayer.getLayerModel();
    view.removeLayer(selectedLayer);
    selectedLayer = null;
    listener.removeLayer(layer);
  }

  /**
   * Called when trying to move selected layer up.
   */
  public void onMoveLayerUpButtonSelection() {
    if (selectedLayer == null)
      return;
    int index = view.getLayerIndex(selectedLayer);
    if (index == -1 || index == 0)
      return;

    view.moveLayerUp(selectedLayer);
    listener.moveLayerUp(selectedLayer.getLayerModel());
  }

  /**
   * Called when trying to move selected layer down.
   */
  public void onMoveLayerDownButtonSelection() {
    if (selectedLayer == null)
      return;
    int size = view.getLayersNumber();
    int index = view.getLayerIndex(selectedLayer);
    if (index == -1 || index == size - 1)
      return;

    view.moveLayerDown(selectedLayer);
    listener.moveLayerDown(selectedLayer.getLayerModel());
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

  private void initContextMenuController() {
    if (contextMenuController == null) {
      contextMenuController = new CreateEditLayersContextMenuController(new CreateEditLayersContextMenuView(), this);
      contextMenuController.bind();
    }
  }

  /**
   * When the selected map is changed, make sure that it's layers are rendered.
   * @param layers
   * LayerModel list.
   */
  public void loadLayers(List<LayerModel> layers) {
    view.removeAllLayers();
    selectedLayer = null;
    if (layers == null || layers.isEmpty())
      return;
    for (LayerModel layer : layers) {
      SelectableLayerView layerView = new SelectableLayerView(layer, this);
      view.addLayer(layerView);
      if (layer.isSelected())
        layerView.select(false, 0, 0);
    }
  }

  @Override
  public void selectedLayerChanged(SelectableLayerView selectedLayer, boolean showContextMenu, double x, double y) {
    if (this.selectedLayer == null)
      this.selectedLayer = selectedLayer;
    else if (this.selectedLayer != selectedLayer) {
      this.selectedLayer.unselect();
      this.selectedLayer = selectedLayer;
    }

    if (showContextMenu && this.selectedLayer != null) {
      initContextMenuController();
      contextMenuController.getContextMenu().hide();
      contextMenuController.getContextMenu().show(this.selectedLayer, x, y);
    }

    listener.selectedLayerChanged(this.selectedLayer != null ? this.selectedLayer.getLayerModel() : null);
  }

  @Override
  public void checkedLayerChanged(LayerModel layer) {
    listener.checkedLayerChanged(layer);
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
}

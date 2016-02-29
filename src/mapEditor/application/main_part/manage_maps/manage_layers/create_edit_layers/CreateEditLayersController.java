package mapEditor.application.main_part.manage_maps.manage_layers.create_edit_layers;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.LayerModel;
import mapEditor.application.main_part.app_utils.models.LayerType;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.util.List;

/**
 *
 * Created by razvanolar on 24.02.2016.
 */
public class CreateEditLayersController implements Controller {

  public interface ICreateEditLayersView extends View {
    TextField getLayerNameTextField();
    ComboBox<LayerType> getLayerTypeComboBox();
  }

  private ICreateEditLayersView view;
  private LayerModel layerModel;
  private Button completeSelectionButton;
  private List<LayerModel> layers;

  public CreateEditLayersController(ICreateEditLayersView view, LayerModel layerModel,
                                    Button completeSelectionButton, List<LayerModel> layers) {
    this.view = view;
    this.layerModel = layerModel;
    this.completeSelectionButton = completeSelectionButton;
    this.layers = layers;
  }

  @Override
  public void bind() {
    addListeners();
    if (layerModel == null) {
      completeSelectionButton.setText("Add");
      completeSelectionButton.setDisable(true);
      view.getLayerTypeComboBox().getSelectionModel().select(LayerType.BACKGROUND);
    } else {
      completeSelectionButton.setText("Edit");
      view.getLayerNameTextField().setText(layerModel.getName());
      view.getLayerTypeComboBox().getSelectionModel().select(layerModel.getType());
    }
  }

  private void addListeners() {
    view.getLayerNameTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      if (!StringValidator.isNullOrEmpty(newValue)) {
        boolean newValueExist = checkIfExist(newValue);
        if (layerModel == null)
          completeSelectionButton.setDisable(newValueExist);
        else
          completeSelectionButton.setDisable(newValueExist && !layerModel.getName().equals(newValue));
      } else
        completeSelectionButton.setDisable(true);
    });
  }

  private boolean checkIfExist(String name) {
    if (layers == null)
      return false;
    for (LayerModel model : layers) {
      if (model.getName().equals(name))
        return true;
    }
    return false;
  }

  public LayerModel getModel() {
    String name = view.getLayerNameTextField().getText();
    LayerType type = view.getLayerTypeComboBox().getSelectionModel().getSelectedItem();
    if (layerModel == null)
      return new LayerModel(name, type);
    layerModel.setName(name);
    layerModel.setType(type);
    return layerModel;
  }
}

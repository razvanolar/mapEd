package mapEditor.application.main_part.manage_maps.layers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.models.LayerType;

/**
 *
 * Created by razvanolar on 24.02.2016.
 */
public class CreateEditLayersView implements CreateEditLayersController.ICreateEditLayersView {

  private GridPane mainContainer;
  private TextField layerNameTextField;
  private ComboBox<LayerType> layerTypeComboBox;

  public CreateEditLayersView() {
    initGUI();
  }

  private void initGUI() {
    layerNameTextField = new TextField();
    layerTypeComboBox = new ComboBox<>();
    mainContainer = new GridPane();

    layerTypeComboBox.getItems().addAll(LayerType.values());
    layerTypeComboBox.setPrefWidth(200);

    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setHgap(5);
    mainContainer.setVgap(5);
    mainContainer.setPadding(new Insets(5));
    mainContainer.add(new Text("Name: "), 0, 0);
    mainContainer.add(layerNameTextField, 1, 0);
    mainContainer.add(new Text("Type: "), 0, 1);
    mainContainer.add(layerTypeComboBox, 1, 1);
  }

  public TextField getLayerNameTextField() {
    return layerNameTextField;
  }

  public ComboBox<LayerType> getLayerTypeComboBox() {
    return layerTypeComboBox;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

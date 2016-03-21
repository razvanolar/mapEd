package mapEditor.application.main_part.manage_maps.utils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.types.Controller;

/**
 *
 * Created by razvanolar on 21.03.2016.
 */
public class RenameMapController implements Controller {

  private HBox container;
  private TextField nameTextField;
  private Button completeSelectionButton;
  private String oldName;

  public RenameMapController(String oldName, Button completeSelectionButton) {
    this.oldName = oldName;
    this.completeSelectionButton = completeSelectionButton;
  }

  @Override
  public void bind() {
    nameTextField = new TextField(oldName);
    nameTextField.setMinWidth(220);
    completeSelectionButton.setDisable(true);
    addListeners();
  }

  private void addListeners() {
    nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      completeSelectionButton.setDisable(!isValidMapName(newValue));
    });
  }

  private boolean isValidMapName(String name) {
    return FileExtensionUtil.isMapFile(name) && !name.equals(oldName);
  }

  public String getMapName() {
    return nameTextField.getText();
  }

  public Node getView() {
    if (container == null) {
      container = new HBox(5, new Text("Name : "), nameTextField);
      container.setAlignment(Pos.CENTER);
    }
    return container;
  }
}

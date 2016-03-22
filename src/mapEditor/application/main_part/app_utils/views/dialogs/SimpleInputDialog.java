package mapEditor.application.main_part.app_utils.views.dialogs;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

/**
 *
 * Created by razvanolar on 22.03.2016.
 */
public class SimpleInputDialog {

  private OkCancelDialog dialog;
  private TextField textField;
  private String title;
  private String labelText;

  public SimpleInputDialog(String title, String labelText) {
    this.title = title;
    this.labelText = labelText;
    initGUI();
    addListeners();
  }

  private void initGUI() {
    textField = new TextField();
    HBox container = new HBox(5, new Text(labelText + " : "), textField);
    container.setAlignment(Pos.CENTER);
    dialog = new OkCancelDialog(title, StageStyle.UTILITY, Modality.APPLICATION_MODAL, false, 5);
    dialog.setContent(container);
    textField.setMinWidth(180);
  }

  private void addListeners() {
    dialog.getOkButton().setOnAction(event -> dialog.close());
  }

  public String showAndWait() {
    dialog.showAndWait();
    return textField.getText();
  }
}

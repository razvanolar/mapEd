package mapEditor.application.main_part.app_utils.views.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;

/**
 *
 * Created by razvanolar on 13.02.2016.
 */
public class Dialog {

  public static void showAlertDialog(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(StringValidator.isNullOrEmpty(title) ? "Warning" : title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static boolean showConfirmDialog(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(StringValidator.isNullOrEmpty(title) ? "Confirm" : title);
    alert.setContentText(message);
    alert.showAndWait();
    return alert.getResult() == ButtonType.OK;
  }
}

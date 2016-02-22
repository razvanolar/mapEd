package mapEditor.application.main_part.app_utils.views.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;

/**
 *
 * Created by razvanolar on 13.02.2016.
 */
public class Dialog {

  public enum DialogButtonType {
    YES, NO
  }

  public static void showAlertDialog(String title, String message) {
    title = StringValidator.isNullOrEmpty(title) ? "Warning" : title;
    Alert alert = createAlertDialog(Alert.AlertType.WARNING, title, message);
    alert.showAndWait();
  }

  public static boolean showConfirmDialog(String title, String message) {
    title = StringValidator.isNullOrEmpty(title) ? "Confirm" : title;
    Alert alert = createAlertDialog(Alert.AlertType.CONFIRMATION, title, message);
    alert.showAndWait();
    return alert.getResult() == ButtonType.OK;
  }

  public static void showInformDialog(String title, String message) {
    title = StringValidator.isNullOrEmpty(title) ? "Info" : title;
    Alert alert = createAlertDialog(Alert.AlertType.INFORMATION, title, message);
    alert.showAndWait();
  }

  public static boolean showYesNoDialog(String title, String message) {
    title = StringValidator.isNullOrEmpty(title) ? "Info" : title;
    return YesNoDialog.getInstance().showAndWait(title, message);
  }

  private static Alert createAlertDialog(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setContentText(message);
    return alert;
  }
}

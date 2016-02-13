package mapEditor.application.main_part.app_utils.views.dialogs;

import javafx.scene.control.Alert;

/**
 *
 * Created by razvanolar on 13.02.2016.
 */
public class AlertDialog {

  public static void showDialog(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title == null ? "Warning" : title);
    alert.setContentText(message);
    alert.showAndWait();
  }
}

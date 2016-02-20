package mapEditor.application.main_part.app_utils.threads;

import javafx.application.Platform;
import javafx.scene.control.Button;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.MessageType;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.repo.RepoController;
import mapEditor.application.repo.SystemParameters;

/**
 *
 * Created by razvanolar on 12.02.2016.
 */
public class MessageHandler {

  private final RepoController repoController = RepoController.getInstance();

  public void handleMessage(MessageType messageType) {
    switch (messageType) {
      case SAVE_TILE_SET_IMAGE:
        new Thread(saveTileSetImage()).start();
        break;
    }
  }

  private Runnable saveTileSetImage() {
    return () -> {
      System.out.println("saveTileSetImage handler started");
      synchronized (repoController) {
        String name = SystemParameters.MESSAGE_KEY.getName();
        String toPath = SystemParameters.MESSAGE_KEY.getPath();
        String imagePath = SystemParameters.MESSAGE_KEY.getImagePath();
        Button button = SystemParameters.MESSAGE_KEY.getButton();
        ImageModel image = SystemParameters.MESSAGE_KEY.getImageModel();
        String imageName = repoController.copyToPath(imagePath, toPath, name);
        if (imageName == null)
          showWarningDialog(null, "Unable to copy the image in the tile_sets directory.");
        else {
          toPath = toPath.endsWith("\\") ? toPath : toPath + "\\";
          button.setDisable(true);
          image.setImageName(imageName);
          image.setImagePath(toPath + imageName);
        }
      }
    };
  }

  private void showWarningDialog(String title, String message) {
    Platform.runLater(() -> Dialog.showAlertDialog(title, message));
  }
}

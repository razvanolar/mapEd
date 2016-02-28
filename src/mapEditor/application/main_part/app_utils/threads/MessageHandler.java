package mapEditor.application.main_part.app_utils.threads;

import javafx.application.Platform;
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
        new Thread(saveTileSetImage(false)).start();
        break;
      case OVERWRITE_TILE_SET_IMAGE:
        new Thread(saveTileSetImage(true)).start();
        break;
    }
  }

  private Runnable saveTileSetImage(boolean overwriteImage) {
    return () -> {
      System.out.println("saveTileSetImage handler started");
      synchronized (repoController) {
        String name = SystemParameters.MESSAGE_KEY.getName();
        String toPath = SystemParameters.MESSAGE_KEY.getPath();
        ImageModel image = SystemParameters.MESSAGE_KEY.getImageModel();
        String imageName = repoController.saveImage(image.getImage(), toPath, name, overwriteImage);
        if (imageName == null)
          showWarningDialog(null, "Unable to copy the image in the tile_sets directory.");
        else {
          toPath = toPath.endsWith("\\") ? toPath : toPath + "\\";
          image.setImageName(imageName);
          image.setImagePath(toPath + imageName);
        }
      }
    };
  }

  private void showWarningDialog(String title, String message) {
    Platform.runLater(() -> Dialog.showWarningDialog(title, message));
  }
}

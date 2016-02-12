package mapEditor.application.main_part.app_utils.threads;

import javafx.scene.control.Button;
import mapEditor.application.main_part.app_utils.models.MessageType;
import mapEditor.application.repo.RepoController;
import mapEditor.application.repo.SystemParameters;

import java.io.File;
import java.util.List;

/**
 *
 * Created by razvanolar on 12.02.2016.
 */
public class MessageHandler {

  private final RepoController repoController = RepoController.getInstance();

  public void handleMessage(MessageType messageType) {
    switch (messageType) {
      case VERIFY_CANVAS_TILE_SET_IMAGE:
        new Thread(verifyCanvasTileSetImage()).start();
        break;
    }
  }

  private Runnable verifyCanvasTileSetImage() {
    return () -> {
      System.out.println("verifyCanvasTileSetImage handler has started");
      synchronized (repoController) {
        String imagePath = SystemParameters.MESSAGE_KEY.getImagePath();
        String pathToVerify = SystemParameters.MESSAGE_KEY.getPathToVerify();
        Button button = SystemParameters.MESSAGE_KEY.getButton();

        List<File> result = repoController.loadLeafItemsFromPath(pathToVerify);

        if (result == null) {
          button.setDisable(true);
          System.out.println("verifyCanvasTileSetImage finished (1)");
          return;
        }

        for (File file : result) {
          if (file.getAbsolutePath().equals(imagePath)) {
            button.setDisable(true);
            System.out.println("verifyCanvasTileSetImage finished (2)");
            return;
          }
        }

        button.setDisable(false);
        System.out.println("verifyCanvasTileSetImage finished (3)");
      }
    };
  }
}

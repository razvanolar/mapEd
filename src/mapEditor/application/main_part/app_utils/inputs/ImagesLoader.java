package mapEditor.application.main_part.app_utils.inputs;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import mapEditor.application.main_part.app_utils.models.ImageModel;

import java.io.File;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ImagesLoader {

  private static ImagesLoader INSTANCE;
  /* TODO: use a configurable path */
  private static String DEFAULT_PATH = "C:\\Users\\razvanolar\\Desktop\\MapEditor_Util\\tilesets";

  public void loadImage(Callback<Image, Void> callback, Window owner) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
    File file = fileChooser.showOpenDialog(owner);
    if (file != null && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
      callback.call(ImageProvider.getImage(file));
    }
  }

  public void loadImageModel(Callback<ImageModel, Void> callback, Window owner) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
    File file = fileChooser.showOpenDialog(owner);
    if (file != null && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
      callback.call(new ImageModel(ImageProvider.getImage(file), file));
    }
  }

  public static ImagesLoader getInstance() {
    if (INSTANCE == null)
      INSTANCE = new ImagesLoader();
    return INSTANCE;
  }
}

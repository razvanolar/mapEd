package mapEditor.application.main_part.app_utils.inputs;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import mapEditor.application.main_part.app_utils.models.ImageModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class FilesLoader {

  private static FilesLoader INSTANCE;
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

  public void loadFile(Callback<File, Void> callback, Window owner) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
    File file = fileChooser.showOpenDialog(owner);
    if (file != null && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
      callback.call(file);
    }
  }

  public void loadFiles(Callback<List<File>, Void> callback, Window owner) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
    List<File> files = fileChooser.showOpenMultipleDialog(owner);
    if (files != null) {
      List<File> result = files.stream().filter(file -> FileExtensionUtil.isImageFile(file.getName())).collect(Collectors.toList());
      callback.call(result);
    }
    callback.call(null);
  }

  public static FilesLoader getInstance() {
    if (INSTANCE == null)
      INSTANCE = new FilesLoader();
    return INSTANCE;
  }
}

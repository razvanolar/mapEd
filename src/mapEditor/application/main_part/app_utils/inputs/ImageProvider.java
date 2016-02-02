package mapEditor.application.main_part.app_utils.inputs;

import javafx.scene.image.Image;

import java.io.File;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ImageProvider {

  public static final String PATH = "resources\\pictures\\";

  public static Image getImage(File file) {
    return file.exists() ? new Image("file:///" + file.getAbsolutePath().replace("\\", "/")) : null;
  }
}

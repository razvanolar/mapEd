package mapEditor.application.main_part.app_utils.inputs;

import javafx.scene.image.Image;
import mapEditor.application.main_part.app_utils.models.ImageModel;

import java.io.File;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ImageProvider {

  public static final String PATH = "resources\\pictures\\";

  public static Image logo() {
    return getImage(new File(PATH + "logo.png"));
  }

  public static Image logo12() {
    return getImage(new File(PATH + "logo_12.png"));
  }

  public static Image logoText60() {
    return getImage(new File(PATH + "logo_text_60.png"));
  }

  public static Image openFolder() {
    return getImage(new File(PATH + "open_folder.png"));
  }

  public static Image closeFolder() {
    return getImage(new File(PATH + "close_folder.png"));
  }

  public static Image genericFile() {
    return getImage(new File(PATH + "generic_file.png"));
  }

  public static Image getImage(File file) {
    return file.exists() ? new Image("file:///" + file.getAbsolutePath().replace("\\", "/")) : null;
  }

  public static ImageModel getImageModel(File file) {
    Image image = getImage(file);
    return image != null ? new ImageModel(image, file.getParentFile() != null ? file.getParentFile().getAbsolutePath() : "", file.getName()) : null;
  }
}

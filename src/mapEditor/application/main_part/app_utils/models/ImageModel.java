package mapEditor.application.main_part.app_utils.models;

import javafx.scene.image.Image;

import java.io.File;

/**
 *
 * Created by razvanolar on 30.01.2016.
 */
public class ImageModel {

  private Image image;
  private String name;
  private String path;
  private File file;

  public ImageModel(Image image, File file) {
    this.image = image;
    this.file = file;
    this.path = refinePath(file.getParentFile().getAbsolutePath());
    this.name = file.getName();
  }

  public ImageModel(Image image, String path, String name) {
    this.image = image;
    this.path = refinePath(path);
    this.name = name;
  }

  public Image getImage() {
    return image;
  }

  public String getPath() {
    return path;
  }

  public String getName() {
    return name;
  }

  public void setPath(String imagePath) {
    this.path = refinePath(imagePath);
    this.file = null;
  }

  public void setName(String imageName) {
    this.name = imageName;
    this.file = null;
  }

  public File getFile() {
    if (file == null && path != null && name != null)
      file = new File(path + name);
    return file;
  }

  private String refinePath(String pathValue) {
    if (pathValue != null && !pathValue.endsWith("\\"))
      pathValue += "\\";
    return pathValue;
  }

  @Override
  public String toString() {
    return file != null ? file.toString() : "";
  }
}

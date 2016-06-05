package mapEditor.application.main_part.app_utils.models;

import javafx.scene.image.Image;

import java.io.File;

/**
 *
 * Created by razvanolar on 30.01.2016.
 */
public class ImageModel extends AbstractDrawModel {

  // not always valid, used only when the tileset of a map is computed
  private int id;

  private Image image;
  private String path;

  public ImageModel(Image image) {
    super(DrawModelType.TILE);
    this.image = image;
  }

  public ImageModel(Image image, File file) {
    super(DrawModelType.TILE);
    this.image = image;
    this.file = file;
    this.path = refinePath(file.getParentFile().getAbsolutePath());
    this.name = file.getName();
  }

  public ImageModel(Image image, String path, String name) {
    super(DrawModelType.TILE);
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

  public void setPath(String imagePath) {
    this.path = refinePath(imagePath);
    this.file = null;
  }

  public void setName(String imageName) {
    this.name = imageName;
    this.file = null;
  }

  public void setImage(Image image) {
    this.image = image;
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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof ImageModel))
      return false;
    ImageModel model = (ImageModel) obj;
    return !(getFile() == null || model.getFile() == null) && getFile().getAbsolutePath().equalsIgnoreCase(model.getFile().getAbsolutePath());
  }

  @Override
  public String toString() {
    return file != null ? file.toString() : "";
  }
}

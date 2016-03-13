package mapEditor.application.main_part.manage_images.manage_tiles.utils;

import javafx.scene.image.Image;
import mapEditor.application.main_part.app_utils.models.ImageModel;

/**
 *
 * Created by razvanolar on 12.03.2016.
 */
public class ImageModelWrapper {

  private ImageModel model;
  private String name;
  private String path;

  public ImageModelWrapper(ImageModel model) {
    this.model = model;
    this.name = model.getName();
    this.path = model.getPath();
  }

  public ImageModel getModel() {
    return model;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public Image getImage() {
    return model.getImage();
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ImageModel computeModel() {
    model.setName(name);
    model.setPath(path);
    return model;
  }
}

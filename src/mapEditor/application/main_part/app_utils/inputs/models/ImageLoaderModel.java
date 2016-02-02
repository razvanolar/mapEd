package mapEditor.application.main_part.app_utils.inputs.models;

import javafx.scene.image.Image;

/**
 *
 * Created by razvanolar on 30.01.2016.
 */
public class ImageLoaderModel {

  private Image image;
  private String imagePath;

  public ImageLoaderModel(Image image, String imagePath) {
    this.image = image;
    this.imagePath = imagePath;
  }

  public Image getImage() {
    return image;
  }

  public String getImagePath() {
    return imagePath;
  }
}

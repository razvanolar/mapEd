package mapEditor.application.main_part.app_utils.models;

import javafx.scene.image.Image;

/**
 *
 * Created by razvanolar on 30.01.2016.
 */
public class ImageLoaderModel {

  private Image image;
  private String imagePath;
  private String imageName;

  public ImageLoaderModel(Image image, String imagePath, String imageName) {
    this.image = image;
    this.imagePath = imagePath;
    this.imageName = imageName;
  }

  public Image getImage() {
    return image;
  }

  public String getImagePath() {
    return imagePath;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }
}

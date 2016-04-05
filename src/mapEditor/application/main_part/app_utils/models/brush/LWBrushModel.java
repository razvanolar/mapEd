package mapEditor.application.main_part.app_utils.models.brush;

import javafx.scene.image.Image;
import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;

import java.util.List;

/**
 *
 * Created by razvanolar on 04.04.2016.
 */
public class LWBrushModel {

  private Image primaryImage;
  private List<Image> otherImages;
  private List<Image> cornerImages;
  private Image previewImage;
  private String name;

  public LWBrushModel(Image primaryImage, List<Image> otherImages, List<Image> cornerImages, Image previewImage) {
    this.primaryImage = primaryImage;
    this.otherImages = otherImages;
    this.cornerImages = cornerImages;
    this.previewImage = previewImage;
  }

  public Image getPrimaryImage() {
    return primaryImage;
  }

  public List<Image> getOtherImages() {
    return otherImages;
  }

  public List<Image> getCornerImages() {
    return cornerImages;
  }

  public Image getPreviewImage() {
    return previewImage;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

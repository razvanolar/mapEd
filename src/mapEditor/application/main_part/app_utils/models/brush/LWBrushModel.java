package mapEditor.application.main_part.app_utils.models.brush;

import javafx.scene.image.Image;

import java.util.List;

/**
 *
 * Created by razvanolar on 04.04.2016.
 */
public class LWBrushModel {

  private Image primaryImage;
  private List<Image> otherImages;
  private Image previewImage;
  private String name;

  public LWBrushModel(Image primaryImage, List<Image> otherImages, Image previewImage) {
    this.primaryImage = primaryImage;
    this.otherImages = otherImages;
    this.previewImage = previewImage;
  }

  public Image getPrimaryImage() {
    return primaryImage;
  }

  public List<Image> getOtherImages() {
    return otherImages;
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

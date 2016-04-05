package mapEditor.application.main_part.app_utils.models.brush;

import javafx.scene.image.Image;

/**
 *
 * Created by razvanolar on 04.04.2016.
 */
public class LWBrushModel {

  private Image[][] primaryMatrix;
  private Image[][] secondaryMatrix;
  private Image previewImage;
  private String name;

  private int primaryImageX;
  private int primaryImageY;

  public LWBrushModel(Image[][] primaryMatrix, Image[][] secondaryMatrix, Image previewImage, int primaryImageX, int primaryImageY) {
    this.primaryMatrix = primaryMatrix;
    this.secondaryMatrix = secondaryMatrix;
    this.previewImage = previewImage;
    this.primaryImageX = primaryImageX;
    this.primaryImageY = primaryImageY;
  }

  public Image getPrimaryImage() {
    if (primaryMatrix != null)
      return primaryMatrix[primaryImageY][primaryImageX];
    return null;
  }

  public Image[][] getPrimaryMatrix() {
    return primaryMatrix;
  }

  public Image[][] getSecondaryMatrix() {
    return secondaryMatrix;
  }

  public int getPrimaryImageX() {
    return primaryImageX;
  }

  public int getPrimaryImageY() {
    return primaryImageY;
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

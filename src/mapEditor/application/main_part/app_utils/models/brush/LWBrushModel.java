package mapEditor.application.main_part.app_utils.models.brush;

import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.repo.SystemParameters;

/**
 *
 * Created by razvanolar on 04.04.2016.
 */
public class LWBrushModel {

  private ImageModel[][] primaryMatrix;
  private ImageModel[][] secondaryMatrix;
  private ImageModel previewImage;
  private String name;

  private int primaryImageX;
  private int primaryImageY;

  public LWBrushModel() {
    primaryMatrix = new ImageModel[SystemParameters.PRIMARY_BRUSH_ROWS][SystemParameters.PRIMARY_BRUSH_COLUMNS];
    secondaryMatrix = new ImageModel[SystemParameters.SECONDARY_BRUSH_ROWS][SystemParameters.SECONDARY_BRUSH_COLUMNS];
  }

  public LWBrushModel(ImageModel[][] primaryMatrix, ImageModel[][] secondaryMatrix, ImageModel previewImage, int primaryImageX, int primaryImageY) {
    this.primaryMatrix = primaryMatrix;
    this.secondaryMatrix = secondaryMatrix;
    this.previewImage = previewImage;
    this.primaryImageX = primaryImageX;
    this.primaryImageY = primaryImageY;
  }

  public ImageModel getPrimaryImage() {
    if (primaryMatrix != null)
      return primaryMatrix[primaryImageY][primaryImageX];
    return null;
  }

  public ImageModel[][] getPrimaryMatrix() {
    return primaryMatrix;
  }

  public ImageModel[][] getSecondaryMatrix() {
    return secondaryMatrix;
  }

  public int getPrimaryImageX() {
    return primaryImageX;
  }

  public int getPrimaryImageY() {
    return primaryImageY;
  }

  public ImageModel getPreviewImage() {
    return previewImage;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setImageForPrimaryMatrix() {

  }

  public void setImageForSecondaryMatrix() {

  }
}

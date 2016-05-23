package mapEditor.application.main_part.app_utils.models.brush;

import mapEditor.application.main_part.app_utils.models.ImageModel;

/**
 * Model containing the information of a single brush tile.
 *
 * Created by razvanolar on 16.04.2016.
 */
public class BrushTileModel {

  private int rowIndex;
  private int colIndex;
  private String path;
  private ImageModel image;

  public BrushTileModel(int rowIndex, int colIndex, String path) {
    this.rowIndex = rowIndex;
    this.colIndex = colIndex;
    this.path = path;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public int getColIndex() {
    return colIndex;
  }

  public String getPath() {
    return path;
  }

  public ImageModel getImageModel() {
    return image;
  }

  public void setImageModel(ImageModel image) {
    this.image = image;
  }
}

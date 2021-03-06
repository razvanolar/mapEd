package mapEditor.application.main_part.app_utils.models.object;

import mapEditor.application.main_part.app_utils.models.AbstractDrawModel;
import mapEditor.application.main_part.app_utils.models.ImageModel;

/**
 *
 * Created by razvanolar on 05.06.2016.
 */
public class ObjectModel extends AbstractDrawModel {

  public enum ObjectTilePlace {
    OBJECT, FOREGROUND
  }

  private ObjectTileModel[][] objectTileModels;

  private ImageModel previewImageModel;
  private String previewImagePath;
  private int primaryTileX;
  private int primaryTileY;
  private int rows;
  private int cols;

  private boolean haveForegroundTiles;

  public ObjectModel(int rows, int cols) {
    super(DrawModelType.OBJECT);
    this.rows = rows;
    this.cols = cols;
  }

  public ObjectTileModel[][] getObjectTileModels() {
    return objectTileModels;
  }

  public void setObjectTileModels(ObjectTileModel[][] objectTileModels) {
    this.objectTileModels = objectTileModels;
    boolean value = false;
    for (int i = 0; i < rows; i ++) {
      for (int j = 0; j < cols; j ++) {
        if (!objectTileModels[i][j].isSolid()) {
          value = true;
          break;
        }
      }
      if (value)
        break;
    }
    this.haveForegroundTiles = value;
  }

  public ImageModel getPreviewImageModel() {
    return previewImageModel;
  }

  public void setPreviewImageModel(ImageModel previewImageModel) {
    this.previewImageModel = previewImageModel;
  }

  public String getPreviewImagePath() {
    return previewImagePath;
  }

  public void setPreviewImagePath(String previewImagePath) {
    this.previewImagePath = previewImagePath;
  }

  public int getPrimaryTileX() {
    return primaryTileX;
  }

  public void setPrimaryTileX(int primaryTileX) {
    this.primaryTileX = primaryTileX;
  }

  public int getPrimaryTileY() {
    return primaryTileY;
  }

  public void setPrimaryTileY(int primaryTileY) {
    this.primaryTileY = primaryTileY;
  }

  public ObjectTileModel getPrimaryTile() {
    return getObjectTileModel(primaryTileY, primaryTileX);
  }

  public ObjectTileModel getObjectTileModel(int row, int col) {
    if (!isInBounds(row, col))
      return null;
    return objectTileModels[row][col];
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  public String getPath() {
    return file.getAbsolutePath();
  }

  public boolean isHaveForegroundTiles() {
    return haveForegroundTiles;
  }

  private boolean isInBounds(int row, int col) {
    return objectTileModels != null && !(row < 0 || row > objectTileModels.length - 1 || col < 0 || col > objectTileModels[0].length - 1);
  }
}

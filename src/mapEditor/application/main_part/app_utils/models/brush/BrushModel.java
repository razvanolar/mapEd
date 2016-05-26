package mapEditor.application.main_part.app_utils.models.brush;

import mapEditor.application.main_part.app_utils.models.AbstractDrawModel;
import mapEditor.application.main_part.app_utils.models.ImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Model containing full XML tiles hierarchy for a brush.
 *
 * Created by razvanolar on 16.04.2016.
 */
public class BrushModel extends AbstractDrawModel {

  public static final int PRIMARY_MATRIX_SIZE = 3;
  public static final int SECONDARY_MATRIX_SIZE = 2;
  public static final int PRIMARY_TILE_NUMBER = 5;

  // the offset of the primary cell relative to left and top margins
  public static final int OFFSET = 1;

  private int primaryImageX;
  private int primaryImageY;
  private String previewImagePath;
  private String name;
  private BrushTileModel[][] primaryMatrix;
  private List<BrushTileModel> primaryTiles;
  private BrushTileModel[][] secondaryMatrix;
  private List<BrushTileModel> secondaryTiles;
  private ImageModel primaryImageModel;

  public BrushModel() {
    super(DrawModelType.BRUSH);
    primaryMatrix = new BrushTileModel[PRIMARY_MATRIX_SIZE][PRIMARY_MATRIX_SIZE];
    secondaryMatrix = new BrushTileModel[SECONDARY_MATRIX_SIZE][SECONDARY_MATRIX_SIZE];
    primaryTiles = new ArrayList<>(PRIMARY_MATRIX_SIZE * PRIMARY_MATRIX_SIZE);
    secondaryTiles = new ArrayList<>(SECONDARY_MATRIX_SIZE * SECONDARY_MATRIX_SIZE);
  }

  public void addPrimaryTile(BrushTileModel tileModel) {
    primaryMatrix[tileModel.getRowIndex()][tileModel.getColIndex()] = tileModel;
    primaryTiles.add(tileModel);
  }

  public void addSecondaryTile(BrushTileModel tileModel) {
    secondaryMatrix[tileModel.getRowIndex()][tileModel.getColIndex()] = tileModel;
    secondaryTiles.add(tileModel);
  }

  public List<BrushTileModel> getPrimaryTiles() {
    return primaryTiles;
  }

  public List<BrushTileModel> getSecondaryTiles() {
    return secondaryTiles;
  }

  public int getPrimaryImageX() {
    return primaryImageX;
  }

  public void setPrimaryImageX(int primaryImageX) {
    this.primaryImageX = primaryImageX;
  }

  public int getPrimaryImageY() {
    return primaryImageY;
  }

  public void setPrimaryImageY(int primaryImageY) {
    this.primaryImageY = primaryImageY;
  }

  public String getPreviewImagePath() {
    return previewImagePath;
  }

  public void setPreviewImagePath(String previewImagePath) {
    this.previewImagePath = previewImagePath;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ImageModel getPrimaryImageModel() {
    return primaryImageModel;
  }

  public void setPrimaryImageModel(ImageModel primaryImageModel) {
    this.primaryImageModel = primaryImageModel;
  }

  public BrushTileModel[][] getPrimaryMatrix() {
    return primaryMatrix;
  }

  public BrushTileModel[][] getSecondaryMatrix() {
    return secondaryMatrix;
  }

  /**
   * Returns the number of the specified image from the primary or secondary matrix.
   * @param imageModel ImageModel
   * @return number of the ImageModel, if the model is find in one of the matrix;
   *         -1 otherwise
   */
  public int getNumberForTile(ImageModel imageModel) {
    if (imageModel == null)
      return -1;

    String imageModelPath = imageModel.getFile().getAbsolutePath();
    for (BrushTileModel brushTileModel : primaryTiles) {
      if (brushTileModel.getPath().equalsIgnoreCase(imageModelPath)) {
        return getNumberFromPrimaryCoordinates(brushTileModel);
      }
    }

    for (BrushTileModel brushTileModel : secondaryTiles) {
      if (brushTileModel.getPath().equalsIgnoreCase(imageModelPath)) {
        return getNumberFromSecondaryCoordinates(brushTileModel);
      }
    }

    return -1;
  }

  private int getNumberFromPrimaryCoordinates(BrushTileModel brushTileModel) {
    int row = brushTileModel.getRowIndex();
    int col = brushTileModel.getColIndex();
    if (row == 0)
      return col + 1;
    return row * PRIMARY_MATRIX_SIZE + col + 1;
  }

  private int getNumberFromSecondaryCoordinates(BrushTileModel brushTileModel) {
    int row = brushTileModel.getRowIndex();
    int col = brushTileModel.getColIndex();
    if (row == 0)
      return 10 + col + 1;
    return 10 + row * SECONDARY_MATRIX_SIZE + col + 1;
  }
}

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
    primaryMatrix = new BrushTileModel[3][3];
    secondaryMatrix = new BrushTileModel[2][2];
    primaryTiles = new ArrayList<>(9);
    secondaryTiles = new ArrayList<>(4);
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
}

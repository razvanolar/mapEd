package mapEditor.application.repo.models;

import mapEditor.application.main_part.app_utils.models.ImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Model containing full XML tiles hierarchy for a brush.
 *
 * Created by razvanolar on 16.04.2016.
 */
public class BrushModel {

  private int primaryImageX;
  private int primaryImageY;
  private String previewImagePath;
  private String name;
  private List<BrushTileModel> primaryTiles;
  private List<BrushTileModel> secondaryTiles;
  private ImageModel primaryImageModel;

  public BrushModel() {
    primaryTiles = new ArrayList<>();
    secondaryTiles = new ArrayList<>();
  }

  public void addPrimaryTile(BrushTileModel tileModel) {
    primaryTiles.add(tileModel);
  }

  public void addSecondaryTile(BrushTileModel tileModel) {
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

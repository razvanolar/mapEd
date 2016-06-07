package mapEditor.application.main_part.app_utils.models.object;

import mapEditor.application.main_part.app_utils.models.ImageModel;

/**
 *
 * Created by razvanolar on 05.06.2016.
 */
public class ObjectTileModel {

  private int row;
  private int col;
  private String path;
  private ImageModel image;
  private ObjectModel.ObjectTilePlace place;

  /**
   * Used when the object is cropped (created).
   * @param row row index
   * @param col col index
   * @param image tile image
   * @param place FOREGROUND or OBJECT, depends with which type the tile was tagged
   */
  public ObjectTileModel(int row, int col, ImageModel image, ObjectModel.ObjectTilePlace place) {
    this.row = row;
    this.col = col;
    this.image = image;
    this.place = place;
  }

  /**
   * Used when the object is loaded from the xml file.
   * @param row row index
   * @param col col index
   * @param path full path of the referred tile
   * @param isSolid true is the tile is marked as solid; false otherwise
   */
  public ObjectTileModel(int row, int col, String path, boolean isSolid) {
    this.row = row;
    this.col = col;
    this.path = path;
    this.place = isSolid ? ObjectModel.ObjectTilePlace.OBJECT : ObjectModel.ObjectTilePlace.FOREGROUND;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ImageModel getImage() {
    return image;
  }

  public void setImage(ImageModel image) {
    this.image = image;
  }

  public ObjectModel.ObjectTilePlace getPlace() {
    return place;
  }

  public void setPlace(ObjectModel.ObjectTilePlace place) {
    this.place = place;
  }

  public boolean isSolid() {
    return place == ObjectModel.ObjectTilePlace.OBJECT;
  }
}

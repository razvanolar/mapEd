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

  public ObjectTileModel(int row, int col, ImageModel image, ObjectModel.ObjectTilePlace place) {
    this.row = row;
    this.col = col;
    this.image = image;
    this.place = place;
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

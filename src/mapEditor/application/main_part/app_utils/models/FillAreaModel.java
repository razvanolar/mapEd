package mapEditor.application.main_part.app_utils.models;

/**
 * Model used to remember an empty cell that will be filled.
 * Created by razvanolar on 22.03.2016.
 */
public class FillAreaModel {

  private int matrixX;
  private int matrixY;
  private int mapX;
  private int mapY;

  public FillAreaModel(int matrixX, int matrixY, int mapX, int mapY) {
    this.matrixX = matrixX;
    this.matrixY = matrixY;
    this.mapX = mapX;
    this.mapY = mapY;
  }

  public int getMatrixX() {
    return matrixX;
  }

  public int getMatrixY() {
    return matrixY;
  }

  public int getMapX() {
    return mapX;
  }

  public int getMapY() {
    return mapY;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof FillAreaModel))
      return false;
    FillAreaModel model = (FillAreaModel) obj;
    return this == obj || (this.matrixX == model.matrixX && this.matrixY == model.matrixY);
  }
}

package mapEditor.application.main_part.app_utils.models;

/**
 * Cell model. Retains the coordinates of a map cell.
 *
 * Created by razvanolar on 08.03.2016.
 */
public class CellModel {
  public int x;
  public int y;

  public CellModel(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof CellModel))
      return false;
    CellModel cell = (CellModel) obj;
    return x == cell.x && y == cell.y;
  }

  @Override
  public String toString() {
    return "x:" + x + " | y:" + y;
  }
}

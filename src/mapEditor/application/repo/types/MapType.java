package mapEditor.application.repo.types;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public enum MapType {
  ORTHOGONAL("orthogonal"), ISOMETRIC("isometric");

  String orientation;

  MapType(String orientation) {
    this.orientation = orientation;
  }

  public String getOrientation() {
    return orientation;
  }
}

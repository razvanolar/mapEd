package mapEditor.application.main_part.manage_maps.visibility_map.utils;

/**
 *
 * Created by razvanolar on 15.03.2016.
 */
public class Point {

  private double x;
  private double y;

  public Point() {}

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  @Override
  public String toString() {
    return "(" + x + " , " + y + ")";
  }

  public void setLocation(double x, double y) {
    this.x = x;
    this.y = y;
  }
}

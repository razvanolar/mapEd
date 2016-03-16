package mapEditor.application.main_part.manage_maps.visibility_map.utils;

/**
 *
 * Created by razvanolar on 15.03.2016.
 */
public class Shape {

  private double[] xPoints;
  private double[] yPoints;
  private int size;

  public Shape(double[] xPoints, double[] yPoints, int size) {
    this.xPoints = xPoints;
    this.yPoints = yPoints;
    this.size = size;
  }

  public double[] getXPoints() {
    return xPoints;
  }

  public double[] getYPoints() {
    return yPoints;
  }

  public int getSize() {
    return size;
  }
}

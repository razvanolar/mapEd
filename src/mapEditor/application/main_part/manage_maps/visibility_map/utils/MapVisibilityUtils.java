package mapEditor.application.main_part.manage_maps.visibility_map.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * Created by razvanolar on 15.03.2016.
 */
public class MapVisibilityUtils {

  private static final double DELTA = 0.0001;
  private static final double EPS = 1e-6;
  private Comparator<Point> pointsComparator;
  public Point lightPosition;

  public List<Line> createRays(Point lightPosition, List<List<Line>> segments) {
    List<Line> rays = new ArrayList<>();
    for (List<Line> lines : segments) {
      for (Line line : lines) {
        Line ray0 = new Line(lightPosition, line.getP1());
        Line ray1 = new Line(lightPosition, line.getP2());
        rays.add(ray0);
        rays.add(ray1);

        rays.add(rotateLine(ray0, +DELTA));
        rays.add(rotateLine(ray0, -DELTA));
        rays.add(rotateLine(ray1, +DELTA));
        rays.add(rotateLine(ray1, -DELTA));
      }
    }
    return rays;
  }

  public Shape createShape(List<Point> closestIntersections) {
    double[] xPoints = new double[closestIntersections.size()];
    double[] yPoints = new double[closestIntersections.size()];
    for (int i=0; i<closestIntersections.size(); i++) {
      Point point = closestIntersections.get(i);
      xPoints[i] = point.getX();
      yPoints[i] = point.getY();
    }
    return new Shape(xPoints, yPoints, closestIntersections.size());
  }

  public List<Point> computeClosestIntersections(List<Line> rays, List<List<Line>> segments) {
    List<Point> intersections = new ArrayList<>();
    for (Line ray : rays) {
      Point closestIntersection = computeIntersection(ray, segments);
      if (closestIntersection != null)
        intersections.add(closestIntersection);
    }
    return intersections;
  }

  private Point computeIntersection(Line ray, List<List<Line>> segments) {
    Point relativeLocation = new Point();
    Point absoluteLocation = new Point();
    Point closestIntersections = null;
    double minDistance = Double.MAX_VALUE;
    for (List<Line> lines : segments) {
      for (Line line : lines) {
        boolean intersect = intersectLines(ray, line, relativeLocation, absoluteLocation);
        if (intersect) {
          if (relativeLocation.getY() >= -EPS && relativeLocation.getY() <= 1 + EPS) {
            if (relativeLocation.getX() >= -EPS && relativeLocation.getX() < minDistance) {
              minDistance = relativeLocation.getX();
              if (closestIntersections == null)
                closestIntersections = new Point();
              closestIntersections.setLocation(absoluteLocation.getX(), absoluteLocation.getY());
            }
          }
        }
      }
    }
    return closestIntersections;
  }

  private boolean intersectLines(Line line0, Line line1, Point relativeLocation, Point absoluteLocation) {
    return intersectLines(line0.getP1().getX(), line0.getP1().getY(),
            line0.getP2().getX(), line0.getP2().getY(),
            line1.getP1().getX(), line1.getP1().getY(),
            line1.getP2().getX(), line1.getP2().getY(),
            relativeLocation, absoluteLocation);
  }

  private boolean intersectLines(double s0x0, double s0y0,
                                 double s0x1, double s0y1,
                                 double s1x0, double s1y0,
                                 double s1x1, double s1y1,
                                 Point relativeLocation,
                                 Point absoluteLocation) {
    double dx0 = s0x1 - s0x0;
    double dy0 = s0y1 - s0y0;
    double dx1 = s1x1 - s1x0;
    double dy1 = s1y1 - s1y0;

    double invLen0 = 1.0 / Math.sqrt(dx0 * dx0 + dy0 * dy0);
    double invLen1 = 1.0 / Math.sqrt(dx1 * dx1 + dy1 * dy1);

    double dir0x = dx0 * invLen0;
    double dir0y = dy0 * invLen0;
    double dir1x = dx1 * invLen1;
    double dir1y = dy1 * invLen1;

    double c0x = s0x0 + dx0 * 0.5;
    double c0y = s0y0 + dy0 * 0.5;
    double c1x = s1x0 + dx1 * 0.5;
    double c1y = s1y0 + dy1 * 0.5;

    double cdx = c1x - c0x;
    double cdy = c1y - c0y;
    double dot = dotPerp(dir0x, dir0y, dir1x, dir1y);
    if (Math.abs(dot) > EPS) {
      if (relativeLocation != null || absoluteLocation != null) {
        double dot0 = dotPerp(cdx, cdy, dir0x, dir0y);
        double dot1 = dotPerp(cdx, cdy, dir1x, dir1y);
        double invDot = 1.0 / dot;
        double s0 = dot1 * invDot;
        double s1 = dot0 * invDot;
        if (relativeLocation != null) {
          double n0 = (s0 * invLen0) + 0.5;
          double n1 = (s1 * invLen1) + 0.5;
          relativeLocation.setLocation(n0, n1);
        }
        if (absoluteLocation != null) {
          double x = c0x + s0 * dir0x;
          double y = c0y + s0 * dir0y;
          absoluteLocation.setLocation(x, y);
        }
      }
      return true;
    }
    return false;
  }

  /**
   * Returns the perpendicular dot product, i.e. the length
   * of the vector (x0,y0,0)x(x1,y1,0).
   *
   * @param x0 Coordinate x0
   * @param y0 Coordinate y0
   * @param x1 Coordinate x1
   * @param y1 Coordinate y1
   * @return The length of the cross product vector
   */
  private static double dotPerp(double x0, double y0, double x1, double y1) {
    return x0 * y1 - y0 * x1;
  }

  private Line rotateLine(Line line, double angle) {
    double x0 = line.getP1().getX();
    double y0 = line.getP1().getY();
    double x1 = line.getP2().getX();
    double y1 = line.getP2().getY();
    double dx = x1 - x0;

    double dy = y1 - y0;
    double sa = Math.sin(angle);
    double ca = Math.cos(angle);
    double nx = ca * dx - sa * dy;
    double ny = sa * dx + ca * dy;

    return new Line(new Point(x0, y0), new Point(x0 + nx, y0 + ny));
  }

  public Comparator<Point> getPointsComparator() {
    if (pointsComparator == null) {
      pointsComparator = (p0, p1) -> {
        double dx0 = p0.getX() - lightPosition.getX();
        double dy0 = p0.getY() - lightPosition.getY();
        double dx1 = p1.getX() - lightPosition.getX();
        double dy1 = p1.getY() - lightPosition.getY();
        double angle0 = Math.atan2(dy0, dx0);
        double angle1 = Math.atan2(dy1, dx1);
        return Double.compare(angle0, angle1);
      };
    }
    return pointsComparator;
  }
}

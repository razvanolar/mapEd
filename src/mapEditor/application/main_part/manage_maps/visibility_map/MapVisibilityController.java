package mapEditor.application.main_part.manage_maps.visibility_map;

import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.models.*;
import mapEditor.application.main_part.manage_maps.MapCanvas;
import mapEditor.application.main_part.manage_maps.utils.MapStoredImagesLoader;
import mapEditor.application.main_part.manage_maps.visibility_map.maps.ShadowMap;
import mapEditor.application.main_part.manage_maps.visibility_map.utils.Line;
import mapEditor.application.main_part.manage_maps.visibility_map.utils.MapVisibilityUtils;
import mapEditor.application.main_part.manage_maps.visibility_map.utils.Point;
import mapEditor.application.main_part.manage_maps.visibility_map.utils.Shape;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * Created by razvanolar on 14.03.2016.
 */
public class MapVisibilityController implements Controller {

  private ChangeListener<Number> sizeChangeListener;

  public interface IMapVisibilityView extends View {
    ScrollPane getScrollPane();
  }

  private IMapVisibilityView view;
  private MapVisibilityUtils utils;
  private MapDetail mapDetail;
  private ShadowMap shadowMap;

  private List<List<Line>> tilesLineSegments;
  private List<Line> borderSegments;
  private Point lightPosition;

  public MapVisibilityController(IMapVisibilityView view, MapDetail mapDetail) {
    this.view = view;
    this.mapDetail = mapDetail;
    this.lightPosition = new Point(0, 0);
    this.utils = new MapVisibilityUtils();
    this.borderSegments = new ArrayList<>();
    this.utils.lightPosition = lightPosition;
  }

  @Override
  public void bind() {
    shadowMap = new ShadowMap(mapDetail);
    tilesLineSegments = new ArrayList<>();
    borderSegments.clear();
    tilesLineSegments.add(borderSegments);
    MapStoredImagesLoader.createTilesContainerFromTilesInfo(shadowMap);
    mapDetail.setZoomStatus(0);

    shadowMap.widthProperty().bind(view.getScrollPane().widthProperty());
    shadowMap.heightProperty().bind(view.getScrollPane().heightProperty());

    view.getScrollPane().setUserData(getSizeChangeListener());
    view.getScrollPane().widthProperty().addListener(getSizeChangeListener());
    view.getScrollPane().heightProperty().addListener(getSizeChangeListener());

    shadowMap.addEventHandler(MouseEvent.MOUSE_PRESSED, shadowMap::onMousePressed);
    shadowMap.addEventFilter(MouseEvent.MOUSE_RELEASED, shadowMap::onMouseReleased);
    shadowMap.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);

    view.getScrollPane().setContent(shadowMap);
    addListeners();
    shadowMap.paint();
    computeObjectSegments();
  }

  private void computeObjectSegments() {
    tilesLineSegments.clear();
    tilesLineSegments.add(borderSegments);
    initBorders();
    int size = shadowMap.getCellWidth();
    List<LayerModel> layers = mapDetail.getLayers();
    MapTilesContainer tilesContainer = shadowMap.getTilesContainer();
    int canvasX = shadowMap.getCanvasX();
    int canvasY = shadowMap.getCanvasY();
    if (layers != null) {
      for (LayerModel layer : layers) {
        if (layer.getType() != LayerType.OBJECT || !layer.isChecked())
          continue;
        ImageModel[][] matrix = tilesContainer.getTilesForLayer(layer);
        if (matrix == null)
          continue;
        for (int i = 0; i < matrix.length; i++) {
          for (int j = 0; j < matrix[i].length; j++) {
            if (matrix[i][j] != null) {
              Point p1 = new Point(j * size + canvasX, i * size + canvasY);
              Point p2 = new Point((j + 1) * size + canvasX, i * size + canvasY);
              Point p3 = new Point((j + 1) * size + canvasX, (i + 1) * size + canvasY);
              Point p4 = new Point(j * size + canvasX, (i + 1) * size + canvasY);

              List<Line> lines = new ArrayList<>();
              lines.add(new Line(p1, p2));
              lines.add(new Line(p2, p3));
              lines.add(new Line(p3, p4));
              lines.add(new Line(p4, p1));

              tilesLineSegments.add(lines);
            }
          }
        }
      }
    }
  }

  private void addListeners() {
    shadowMap.setOnMouseMoved(event -> {
      shadowMap.paint();
      lightPosition.setX(event.getX());
      lightPosition.setY(event.getY());

      GraphicsContext g = shadowMap.getGraphicsContext2D();
      if (shadowMap.checkMouseBorders((int) event.getX(), (int) event.getY())) {
        paintSegments();

        List<Line> rays = utils.createRays(lightPosition, tilesLineSegments);
//      paintRays(rays);

        List<Point> intersections = utils.computeClosestIntersections(rays, tilesLineSegments);
        Collections.sort(intersections, utils.getPointsComparator());
        Shape shape = utils.createShape(intersections);
        g.setFill(new Color(.55, .55, .0, .5));
        g.fillPolygon(shape.getXPoints(), shape.getYPoints(), shape.getSize());
      }

      g.setFill(Color.YELLOW);
      g.fillOval(event.getX() - 5, event.getY() - 5, 10, 10);
    });
  }

  private void onMouseDragged(MouseEvent event) {
    shadowMap.onMouseDragged(event);
    computeObjectSegments();
  }

  private void paintSegments() {
    GraphicsContext g = shadowMap.getGraphicsContext2D();

    g.setStroke(Color.RED);
    for (List<Line> lines : tilesLineSegments) {
      for (Line line : lines) {
        g.strokeLine(line.getP1().getX(),
                line.getP1().getY(),
                line.getP2().getX(),
                line.getP2().getY());
      }
    }
  }

  private void paintRays(List<Line> rays) {
    GraphicsContext g = shadowMap.getGraphicsContext2D();
    int canvasX = shadowMap.getCanvasX();
    int canvasY = shadowMap.getCanvasY();

    g.setStroke(Color.YELLOW);
    for (Line ray : rays)
      g.strokeLine(ray.getP1().getX() + canvasX,
              ray.getP1().getY() + canvasY,
              ray.getP2().getX() + canvasX,
              ray.getP2().getY() + canvasY);
  }

  private void initBorders() {
    borderSegments.clear();
    int canvasX = shadowMap.getCanvasX();
    int canvasY = shadowMap.getCanvasY();
    int mapWidth = shadowMap.getMapWidth();
    int mapHeight = shadowMap.getMapHeight();
    Point p1 = new Point(canvasX, canvasY);
    Point p2 = new Point(canvasX + mapWidth, canvasY);
    Point p3 = new Point(canvasX + mapWidth, canvasY + mapHeight);
    Point p4 = new Point(canvasX, canvasY + mapHeight);
    borderSegments.add(new Line(p1, p2));
    borderSegments.add(new Line(p2, p3));
    borderSegments.add(new Line(p3, p4));
    borderSegments.add(new Line(p4, p1));
  }

  public void setMapDetail(MapDetail mapDetail) {
    this.mapDetail = mapDetail;
    bind();
    initBorders();
  }

  public IMapVisibilityView getView() {
    return view;
  }

  public MapCanvas getShadowMap() {
    return shadowMap;
  }

  public void update() {
    computeObjectSegments();
    shadowMap.paint();
  }

  public void unbindMap() {
    if (shadowMap != null) {
      shadowMap.widthProperty().unbind();
      shadowMap.heightProperty().unbind();
      shadowMap = null;
    }
  }

  private ChangeListener<Number> getSizeChangeListener() {
    if (sizeChangeListener == null) {
      sizeChangeListener = (observable, oldValue, newValue) -> {
        if (shadowMap != null) {
          shadowMap.paint();
          computeObjectSegments();
        }
      };
    }
    return sizeChangeListener;
  }
}

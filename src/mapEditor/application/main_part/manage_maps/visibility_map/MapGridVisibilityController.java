package mapEditor.application.main_part.manage_maps.visibility_map;

import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.models.*;
import mapEditor.application.main_part.manage_maps.utils.MapStoredImagesLoader;
import mapEditor.application.main_part.manage_maps.visibility_map.maps.GridVisibilityMap;
import mapEditor.application.main_part.manage_maps.visibility_map.utils.MapVisibilityConstants;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 19.03.2016.
 */
public class MapGridVisibilityController implements Controller {

  private ChangeListener<Number> sizeChangeListener;
  private int cellX;
  private int cellY;

  public interface IMapGridVisibilityView extends View {
    ScrollPane getScrollPane();
  }

  private IMapGridVisibilityView view;
  private MapDetail mapDetail;
  private GridVisibilityMap gridVisibilityMap;
  private boolean[][] objectsMatrix;
  private boolean[][] fovMatrix;

  public MapGridVisibilityController(IMapGridVisibilityView view, MapDetail mapDetail) {
    this.view = view;
    this.mapDetail = mapDetail;
  }

  @Override
  public void bind() {
    gridVisibilityMap = new GridVisibilityMap(mapDetail);
    MapStoredImagesLoader.createTilesContainerFromTilesInfo(gridVisibilityMap);

    objectsMatrix = new boolean[mapDetail.getRows()][];
    fovMatrix = new boolean[mapDetail.getRows()][];
    for (int i=0; i<mapDetail.getRows(); i++) {
      objectsMatrix[i] = new boolean[mapDetail.getColumns()];
      fovMatrix[i] = new boolean[mapDetail.getColumns()];
    }

    computeObjectMatrix();

    ScrollPane scrollPane = view.getScrollPane();
    gridVisibilityMap.widthProperty().bind(scrollPane.widthProperty());
    gridVisibilityMap.heightProperty().bind(scrollPane.heightProperty());

    gridVisibilityMap.addEventHandler(MouseEvent.MOUSE_PRESSED, gridVisibilityMap::onMousePressed);
    gridVisibilityMap.setOnMouseMoved(this::onMouseMoved);
    gridVisibilityMap.setOnMouseDragged(this::onMouseDragged);
    gridVisibilityMap.setOnMouseReleased(gridVisibilityMap::onMouseReleased);

    scrollPane.setUserData(getSizeChangeListener());
    scrollPane.widthProperty().addListener(getSizeChangeListener());
    scrollPane.heightProperty().addListener(getSizeChangeListener());

    scrollPane.setContent(gridVisibilityMap);
    gridVisibilityMap.paint();
  }

  private void onMouseMoved(MouseEvent event) {
    gridVisibilityMap.onMouseMoved(event);
    if (!isInMapBounds(event))
      return;
    cellX = (int) ((event.getX() - gridVisibilityMap.getCanvasX()) / gridVisibilityMap.getCellWidth());
    cellY = (int) ((event.getY() - gridVisibilityMap.getCanvasY()) / gridVisibilityMap.getCellHeight());
    computeFOVMatrix();
    hoverObjects();
  }

  private void onMouseDragged(MouseEvent event) {
    gridVisibilityMap.onMouseDragged(event);
    if (!isInMapBounds(event))
      return;
    hoverObjects();
  }

  private void hoverObjects() {
    GraphicsContext g = gridVisibilityMap.getGraphicsContext2D();
    Color objectColor = new Color(.5, .5, .0, .6);
    Color hiddenTile = new Color(.5, .5, .5, .6);
    int cellWidth = gridVisibilityMap.getCellWidth();
    int cellHeight = gridVisibilityMap.getCellHeight();
    int canvasX = gridVisibilityMap.getCanvasX();
    int canvasY = gridVisibilityMap.getCanvasY();

    // show the objects
    for (int i=0; i<mapDetail.getRows(); i++) {
      for (int j=0; j<mapDetail.getColumns(); j++) {
        if (objectsMatrix[i][j]) {
          g.setFill(objectColor);
          g.fillRect(canvasX + j * cellWidth, canvasY + i * cellHeight, cellWidth, cellHeight);
        } else if (!fovMatrix[i][j]) {
          g.setFill(hiddenTile);
          g.fillRect(canvasX + j * cellWidth, canvasY + i * cellHeight, cellWidth, cellHeight);
        }
      }
    }
    g.setFill(new Color(.2, .2, .8, .7));
    g.fillRect(cellX * cellWidth + gridVisibilityMap.getCanvasX(), cellY * cellHeight + gridVisibilityMap.getCanvasY(), cellWidth, cellHeight);
  }

  private void computeObjectMatrix() {
    if (mapDetail == null || mapDetail.getLayers() == null || mapDetail.getLayers().isEmpty() ||
            gridVisibilityMap == null || gridVisibilityMap.getTilesContainer() == null)
      return;
    CustomMap<LayerModel, MapTilesContainer.TilesMatrix> tilesMap = gridVisibilityMap.getTilesContainer().getTilesMap();
    if (tilesMap == null || tilesMap.isEmpty())
      return;

    for (LayerModel layer : tilesMap.keys()) {
      if (layer.getType() != LayerType.OBJECT)
        continue;
      MapTilesContainer.TilesMatrix tilesMatrix = tilesMap.get(layer);
      ImageModel[][] tiles = tilesMatrix.getTilesMatrix();
      if (tiles == null)
        continue;
      for (int i=0; i<tiles.length; i++) {
        for (int j=0; j<tiles[i].length; j++) {
          if (tiles[i][j] != null) {
            objectsMatrix[i][j] = true;
          }
        }
      }
    }
  }

  private void computeFOVMatrix() {
    for (int i=0; i<fovMatrix.length; i++) {
      for (int j=0; j<fovMatrix[i].length; j++) {
        fovMatrix[i][j] = false;
      }
    }

    for (int i=0; i<MapVisibilityConstants.RAYS+1; i+=MapVisibilityConstants.STEP) {
      double ax = MapVisibilityConstants.SIN_TABLE[i];
      double ay = MapVisibilityConstants.COS_TABLE[i];

      double hoveredCellX = cellX;
      double hoveredCellY = cellY;

      for (int z=0; z<MapVisibilityConstants.RADIUS; z++) {
        hoveredCellX += ax;
        hoveredCellY += ay;
        int col = (int) hoveredCellX;
        int row = (int) hoveredCellY;
        if (hoveredCellX < 0 || hoveredCellY < 0 || row > mapDetail.getRows()-1 || col > mapDetail.getColumns()-1)
          break;
        fovMatrix[(row)][(col)] = true;
        if (objectsMatrix[(row)][(col)])
          break;
      }
    }
  }

  private boolean isInMapBounds(MouseEvent event) {
    return !(event.getX() < gridVisibilityMap.getCanvasX() ||
            event.getY() < gridVisibilityMap.getCanvasY() ||
            event.getX() > gridVisibilityMap.getMapWidth() + gridVisibilityMap.getCanvasX() ||
            event.getY() > gridVisibilityMap.getMapHeight() + gridVisibilityMap.getCanvasY());
  }

  public void setMapDetail(MapDetail mapDetail) {
    this.mapDetail = mapDetail;
    bind();
  }

  public IMapGridVisibilityView getView() {
    return view;
  }

  public ChangeListener<Number> getSizeChangeListener() {
    if (sizeChangeListener == null) {
      sizeChangeListener = (observable, oldValue, newValue) -> {
        if (gridVisibilityMap != null)
          gridVisibilityMap.paint();
      };
    }
    return sizeChangeListener;
  }
}

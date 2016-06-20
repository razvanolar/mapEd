package mapEditor.application.main_part.manage_maps.primary_map;

import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.models.*;
import mapEditor.application.main_part.app_utils.models.brush.BrushModel;
import mapEditor.application.main_part.app_utils.models.object.ObjectModel;
import mapEditor.application.main_part.app_utils.models.object.ObjectTileModel;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.manage_maps.MapCanvas;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class PrimaryMapView extends MapCanvas {

  public static PrimaryMapUtil mapUtil = new PrimaryMapUtil();

  public PrimaryMapView(MapDetail mapDetail) {
    super(mapDetail.getRows(), mapDetail.getColumns());
    this.mapDetail = mapDetail;
    this.setWidth(100);
    this.setHeight(100);
    this.prefHeight(100);
    this.prefWidth(100);
    initializeComponents();
  }

  private void initializeComponents() {
    fillColor = mapDetail.getBackgroundColor();
    gridColor = mapDetail.getGridColor();
    squareColor = mapDetail.getSquareColor();
    gridEnabled = mapDetail.isShowGrid();
    this.setCache(true);
    this.setCacheHint(CacheHint.SPEED);

    canvasX = mapDetail.getX();
    canvasY = mapDetail.getY();
    zoomStatus = mapDetail.getZoomStatus();

    CELL_WIDTH = DEFAULT_CELL_WIDTH + zoomStatus;
    CELL_HEIGHT = DEFAULT_CELL_HEIGHT + zoomStatus;
  }

  public void onMousePressedEvent(MouseEvent event) {
    super.handleMousePressEvent(event);
    if (event.isControlDown())
      return;
    if (event.isPrimaryButtonDown())
      drawImagesOnMouseActions(event);
  }

  public void onMouseReleasedEvent(MouseEvent event, boolean paintMap) {
    if (dragDetected) {
      dragDetected = false;

      canvasX = getDragHorizontalValue(event);
      canvasY = getDragVerticalValue(event);
      lastValidCanvasX = canvasX;
      lastValidCanvasY = canvasY;

      if(paintMap) {
        paint();
        MapEditorController.getInstance().setSceneCursor(Cursor.DEFAULT);
        notifyPositionChange();
      }
    } else {
      if (!gridEnabled && paintMap)
        paint();
    }
  }

  public void onMouseMovedEvent(MouseEvent event) {
    /* the cartesian coordinates of the hovered cell */
    int x = (int) event.getX() + 1;
    int y = (int) event.getY() + 1;

    if (!checkMouseBorders(x, y))
      return;

    hoveredCellX = (int) ((event.getX() - canvasX) / CELL_WIDTH) * CELL_WIDTH;
    hoveredCellY = (int) ((event.getY() - canvasY) / CELL_HEIGHT) * CELL_HEIGHT;

    if (lastMoveX == hoveredCellX && lastMoveY == hoveredCellY)
      return;

    if (!gridEnabled)
      paint();
    drawGridLines(hoveredCellX, hoveredCellY);

    lastMoveX = hoveredCellX;
    lastMoveY = hoveredCellY;
  }

  public void onMouseDraggedEvent(MouseEvent event) {
    if (event.isControlDown()) {
      super.handleMouseDragEvent(event);
      return;
    }
    if (!event.isPrimaryButtonDown())
      return;

    drawImagesOnMouseActions(event);
  }

  public void onScrollEvent(ScrollEvent event) {
    /* If the mouse position is not situated in the map borders, zoom actions are not applied */
    if(!checkMouseBorders((int) event.getX(), (int) event.getY()))
      return;

    /* the cartesian coordinates of the hovered cell relative to the matrix, not to the origin of the canvas */
    int hoveredCellX = (int) ((event.getX() - canvasX) / CELL_WIDTH) * CELL_WIDTH;
    int hoveredCellY = (int) ((event.getY() - canvasY) / CELL_HEIGHT) * CELL_HEIGHT;

    /* the matrix coordinates of the hovered cell */
    int hoveredCellColumn = hoveredCellX / CELL_WIDTH;
    int hoveredCellRow = hoveredCellY / CELL_HEIGHT;

    //System.out.println("row=" + hoveredCellRow + " col=" + hoveredCellColumn);

    /* if deltaY < 0 -> ZOOM OUT else ZOOM IN */
    zoomAction(event.getDeltaY() < 0 ? -1 : 1, hoveredCellRow, hoveredCellColumn);
  }

  private void zoomAction(int zoomType, int hoveredCellRow, int hoveredCellColumn) {
    if (zoomType != 1 && zoomType != -1)
      return;

    zoomStatus = zoomType == 1 ? zoomStatus + ZOOM_RATIO : zoomStatus - ZOOM_RATIO;
    /* if zoom status runs out of the bound we restore it and end the function call */
    if (zoomStatus < -10 || zoomStatus > 15) {
      zoomStatus = zoomType == 1 ? zoomStatus - ZOOM_RATIO : zoomStatus + ZOOM_RATIO;
      return;
    }

    /* determine the offset of the hovered cell before the cell dimensions are changed */
    int offsetCellX = (hoveredCellColumn * CELL_WIDTH) + canvasX;
    int offsetCellY = (hoveredCellRow * CELL_HEIGHT) + canvasY;

    //System.out.println("offX=" + offsetCellX + " offY=" + offsetCellY);

    CELL_WIDTH = DEFAULT_CELL_WIDTH + zoomStatus;
    CELL_HEIGHT = DEFAULT_CELL_HEIGHT + zoomStatus;

    canvasX = offsetCellX - (hoveredCellColumn * CELL_WIDTH);
    canvasY = offsetCellY - (hoveredCellRow * CELL_HEIGHT);

    //System.out.println("canvasX=" + canvasX + " canvasY=" + canvasY);

    if(isHorizontalDraggable()) {
      if(canvasX > CELL_WIDTH)
        canvasX = CELL_WIDTH;
      else if(canvasX < 0 && ((canvasX + getMapWidth()) - getWidth()) < -CELL_WIDTH) {
        canvasX = (int) getWidth() - getMapWidth() - CELL_WIDTH;
      }
    } else
      canvasX = ((int) getWidth() - getMapWidth()) / 2;

    if(isVerticalDraggable()) {
      if(canvasY > CELL_HEIGHT)
        canvasY = CELL_HEIGHT;
      else if(canvasY < 0 && ((canvasY + getMapHeight()) - getHeight()) < -CELL_HEIGHT) {
        canvasY = (int) getHeight() - getMapHeight() - CELL_HEIGHT;
      }
    } else
      canvasY = ((int) getHeight() - getMapHeight()) / 2;

    lastValidCanvasX = canvasX;
    lastValidCanvasY = canvasY;
    paint();
    notifyPositionChange();
  }

  private void drawGridLines(int hoveredCellX, int hoveredCellY) {
     /* the matrix coordinates of the hovered cell */
    int hoveredCellColumn = hoveredCellX / CELL_WIDTH;
    int hoveredCellRow = hoveredCellY / CELL_HEIGHT;

    //System.out.println("hX=" + hoveredCellColumn + " hY=" + hoveredCellRow);

    int cellX = hoveredCellColumn * CELL_WIDTH + canvasX;
    int cellY = hoveredCellRow * CELL_HEIGHT + canvasY;
    drawGrid(getGraphicsContext2D(), cellX, cellY,
            mapInfo[START_X],
            mapInfo[START_Y],
            mapInfo[STOP_X],
            mapInfo[STOP_Y]);
  }

  private void drawImagesOnMouseActions(MouseEvent event) {
    if (!DELETE_ENTITY) {
      if (selectedLayer == null) {
        Dialog.showWarningDialog(null, "Please select a layer");
        return;
      } else if (!selectedLayer.isChecked()) {
        Dialog.showWarningDialog(null, "Selected layer is not checked");
        return;
      } else if (selectedDrawModel == null) {
        Dialog.showWarningDialog(null, "Please select a tile");
        return;
      }
    }

    /** matrix cells coordinates */
    int cellX = (int) ((event.getX() - canvasX) / CELL_WIDTH);
    int cellY = (int) ((event.getY() - canvasY) / CELL_HEIGHT);

    /** map coordinates */
    int hoveredCellX = (int) ((event.getX() - canvasX) / CELL_WIDTH) * CELL_WIDTH + canvasX + 1;
    int hoveredCellY = (int) ((event.getY() - canvasY) / CELL_HEIGHT) * CELL_HEIGHT + canvasY + 1;

    GraphicsContext g = getGraphicsContext2D();

    if (FILL_AREA && selectedDrawModel.getDrawModelType() == AbstractDrawModel.DrawModelType.TILE)
      fillArea(g, hoveredCellX, hoveredCellY, cellX, cellY);
    else if (DELETE_ENTITY) {
      tilesContainer.addTile(null, selectedLayer, cellY, cellX);
      paintAllCellTilesPerLayer(g, hoveredCellX, hoveredCellY, cellX, cellY);
    } else {
      if (selectedDrawModel.getDrawModelType() == AbstractDrawModel.DrawModelType.TILE && selectedDrawModel instanceof ImageModel) {
        // single tile selected
        tilesContainer.addTile((ImageModel) selectedDrawModel, selectedLayer, cellY, cellX);
        paintAllCellTilesPerLayer(g, hoveredCellX, hoveredCellY, cellX, cellY);
      } else if (selectedDrawModel.getDrawModelType() == AbstractDrawModel.DrawModelType.BRUSH && selectedDrawModel instanceof BrushModel) {
        // brush selected
        BrushModel brush = (BrushModel) selectedDrawModel;
        runBrushAlgorithm(cellX, cellY, brush);
      } else if (selectedDrawModel.getDrawModelType() == AbstractDrawModel.DrawModelType.OBJECT && selectedDrawModel instanceof ObjectModel) {
        // object selected

        if (selectedLayer.getType() != LayerType.OBJECT) {
          Dialog.showWarningDialog(null, "Please select an object type layer");
          return;
        }

        List<LayerModel> layers = mapDetail.getLayers();
        int index = layers.indexOf(selectedLayer);
        if (index == -1) {
          Dialog.showWarningDialog(null, "Index of the selected layer can't be found");
          return;
        }
        LayerModel firstForegroundLayer = null;
        for (int i = index + 1; i < layers.size(); i ++) {
          LayerModel model = layers.get(i);
          if (model != null && model.getType() == LayerType.FOREGROUND) {
            firstForegroundLayer = model;
            break;
          }
        }
        if (firstForegroundLayer == null) {
          Dialog.showWarningDialog(null, "The object is having foreground tiles defined. Please create a foreground layer to follow the current object layer.");
          return;
        }

        ObjectModel objectModel = (ObjectModel) selectedDrawModel;
        drawObjectModel(objectModel, firstForegroundLayer, cellY, cellX);
      }
    }

    drawGridLines(hoveredCellX - canvasX, hoveredCellY - canvasY);
  }

  /**
   * Run draw brush runBrushAlgorithm using PrimaryMapUtil
   * @param cellX hovered cell X value
   * @param cellY hovered cell Y value
   * @param brush BrushModel
   */
  private void runBrushAlgorithm(int cellX, int cellY, BrushModel brush) {
    tilesContainer.createTileMapForLayerIfNotExists(selectedLayer);
    mapUtil.run(cellX, cellY, brush, tilesContainer.getTilesForLayer(selectedLayer));
    paint();
  }

  private void drawObjectModel(ObjectModel object, LayerModel foregroundLayer, int row, int col) {
    int rows = object.getRows();
    int cols = object.getCols();
    int primaryCellY = object.getPrimaryTileY();
    int primaryCellX = object.getPrimaryTileX();
    int gridStartRow = row - primaryCellY;
    int gridStartCol = col - primaryCellX;

    ObjectTileModel[][] objectTileModels = object.getObjectTileModels();
    for (int i = gridStartRow; i < gridStartRow + rows; i ++) {
      for (int j = gridStartCol; j < gridStartCol + cols; j ++) {
        ObjectTileModel tileModel = objectTileModels[i - gridStartRow][j - gridStartCol];
        if (tileModel.isSolid())
          tilesContainer.addTile(tileModel.getImage(), selectedLayer, i, j);
        else
          tilesContainer.addTile(tileModel.getImage(), foregroundLayer, i, j);
      }
    }
    paint();
  }

  private void fillArea(GraphicsContext g, int hoveredCellX, int hoveredCellY, int cellX, int cellY) {
    if (selectedLayer == null ||
            selectedDrawModel.getDrawModelType() != AbstractDrawModel.DrawModelType.TILE ||
            !(selectedDrawModel instanceof ImageModel))
      return;
    ImageModel imageModel = (ImageModel) selectedDrawModel;
    ImageModel[][] tiles = tilesContainer.getTilesForLayer(selectedLayer);
    if (tiles != null && tiles[cellY][cellX] != null) {
      paintAllCellTilesPerLayer(g, hoveredCellX, hoveredCellY, cellX, cellY);
      return;
    }
    Queue<FillAreaModel> queue = new LinkedList<>();
    queue.add(new FillAreaModel(cellX, cellY, hoveredCellX, hoveredCellY));
    while (!queue.isEmpty()) {
      FillAreaModel model = queue.poll();
      int row = model.getMatrixY();
      int col = model.getMatrixX();
      int mapX = model.getMapX();
      int mapY = model.getMapY();

      tilesContainer.addTile(imageModel, selectedLayer, row, col);
      tiles = tilesContainer.getTilesForLayer(selectedLayer);
      paintAllCellTilesPerLayer(g, mapX, mapY, col, row);

      if (checkMatrixBorders(row + 1, col) && tiles[row + 1][col] == null) {
        FillAreaModel modelDown = new FillAreaModel(col, row + 1, mapX, mapY + CELL_HEIGHT);
        if (!queue.contains(modelDown))
          queue.add(modelDown);
      }
      if (checkMatrixBorders(row - 1, col) && tiles[row - 1][col] == null) {
        FillAreaModel modelUp = new FillAreaModel(col, row - 1, mapX, mapY - CELL_HEIGHT);
        if (!queue.contains(modelUp))
          queue.add(modelUp);
      }
      if (checkMatrixBorders(row, col + 1) && tiles[row][col + 1] == null) {
        FillAreaModel modelRight = new FillAreaModel(col + 1, row, mapX + CELL_WIDTH, mapY);
        if (!queue.contains(modelRight))
          queue.add(modelRight);
      }
      if (checkMatrixBorders(row, col - 1) && tiles[row][col - 1] == null) {
        FillAreaModel modelLeft = new FillAreaModel(col - 1, row, mapX - CELL_WIDTH, mapY);
        if (!queue.contains(modelLeft))
          queue.add(modelLeft);
      }
    }
  }

  /**
   * Paint all the tiles for all visible layers for the specified cell.
   * @param g
   * Map GraphicsContext
   * @param hoveredCellX
   * Map X coordinate.
   * @param hoveredCellY
   * Map Y coordinate.
   * @param cellX
   * Matrix X cell coordinate (X Column).
   * @param cellY
   * Matrix Y cell coordinate (Y Row).
   */
  private void paintAllCellTilesPerLayer(GraphicsContext g, int hoveredCellX, int hoveredCellY, int cellX, int cellY) {
    CustomMap<LayerModel, MapTilesContainer.TilesMatrix> map = tilesContainer.getTilesMap();
    if (map != null) {
      g.setFill(fillColor);
      g.clearRect(hoveredCellX, hoveredCellY, CELL_WIDTH, CELL_HEIGHT);
      g.fillRect(hoveredCellX, hoveredCellY, CELL_WIDTH, CELL_HEIGHT);
      for (LayerModel layer : mapDetail.getLayers()) {
        if (!layer.isChecked())
          continue;
        MapTilesContainer.TilesMatrix tilesMatrix = map.get(layer);
        if (tilesMatrix != null) {
          ImageModel[][] matrix = tilesMatrix.getTilesMatrix();
          ImageModel image = matrix[cellY][cellX];
          if (image != null)
            g.drawImage(image.getImage(), hoveredCellX, hoveredCellY, CELL_WIDTH, CELL_HEIGHT);
        }
      }
    }
  }

  public void setCurrentLayer(LayerModel layer) {
    selectedLayer = layer;
  }
}

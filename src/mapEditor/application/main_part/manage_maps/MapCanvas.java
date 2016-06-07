package mapEditor.application.main_part.manage_maps;

import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.models.*;
import mapEditor.application.main_part.app_utils.models.object.ObjectModel;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class MapCanvas extends Canvas {

  protected static int ZOOM_RATIO = 1;
  protected static int DEFAULT_CELL_WIDTH;
  protected static int DEFAULT_CELL_HEIGHT;

  protected int CELL_WIDTH;
  protected int CELL_HEIGHT;

  protected int ROWS;
  protected int COLUMNS;

  protected Color fillColor, gridColor, squareColor;

  protected boolean gridEnabled = true;
  protected double pressedX, pressedY;
  protected int hoveredCellX;
  protected int hoveredCellY;
  /**
   * zoomStatus must be in [-10, 15]
   */
  protected int zoomStatus = 0;

  protected boolean dragDetected;

  /**
   * maintain the coordinates of the last hovered square
   */
  protected int lastMoveX, lastMoveY;

  /** represent the canvas cartesian coordinates; they are updated every time canvas is dragged or zoom action occurs */
  protected int canvasX, canvasY;
  protected int lastValidCanvasX, lastValidCanvasY;

  protected static int MAP_INFO_NB = 11;
  protected int START_INDEX_X = 0;
  protected int START_INDEX_Y = 1;
  protected int STOP_INDEX_X = 2;
  protected int STOP_INDEX_Y = 3;
  protected int START_X = 4;
  protected int START_Y = 5;
  protected int STOP_X = 6;
  protected int STOP_Y = 7;
  protected int CANVAS_X = 8;
  protected int CANVAS_Y = 9;
  protected int FORCE_UPDATE = 10;
  protected int[] mapInfo = new int[MAP_INFO_NB];

  protected AbstractDrawModel selectedDrawModel;
  protected LayerModel selectedLayer;
  protected MapDetail mapDetail;
  protected MapTilesContainer tilesContainer;

  public MapCanvas(int rows, int cols) {
    this(AppParameters.CURRENT_PROJECT.getCellSize(), AppParameters.CURRENT_PROJECT.getCellSize(), rows, cols);
  }

  public MapCanvas(int cellWidth, int cellHeight, int rows, int cols) {
    super();
    DEFAULT_CELL_WIDTH = cellWidth;
    DEFAULT_CELL_HEIGHT = cellHeight;
    CELL_WIDTH = DEFAULT_CELL_WIDTH;
    CELL_HEIGHT = DEFAULT_CELL_HEIGHT;
    ROWS = rows;
    COLUMNS = cols;
    tilesContainer = new MapTilesContainer(ROWS, COLUMNS);
  }

  protected void addListeners() {
    addMouseReleasedListener();
  }

  protected void addMouseDraggedListener() {
    this.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragEvent);
  }

  protected void addMouseReleasedListener() {
    this.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> handleMouseReleaseEvent(event, true));
  }

  protected void handleMouseReleaseEvent(MouseEvent event, boolean paintMap) {
    if (dragDetected) {
      dragDetected = false;

      canvasX = getDragHorizontalValue(event);
      canvasY = getDragVerticalValue(event);
      lastValidCanvasX = canvasX;
      lastValidCanvasY = canvasY;
      updateMapModelDetails();

      /* this 'if' block is same as in the MiniMapCanvas method; be careful when you modified it, probably the one from
       * the child class method needs to be modified also */
      MapEditorController.getInstance().setSceneCursor(Cursor.DEFAULT);
      if(paintMap) {
        paint();
        notifyPositionChange();
      }
    }
  }

  protected void handleMousePressEvent(MouseEvent event) {
    if (!event.isPrimaryButtonDown())
      return;
    pressedX = event.getX();
    pressedY = event.getY();
  }

  /**
   * Handle canvas drag events
   * @param event - MouseEvent
   */
  protected void handleMouseDragEvent(MouseEvent event) {
    if (!event.isPrimaryButtonDown())
      return;
    if (event.isControlDown()) {
      /* if the initial position at which the mouse was pressed is not valid, the map can not be dragged */
      if(!checkMouseBorders())
        return;

      if (!dragDetected)
        MapEditorController.getInstance().setSceneCursor(Cursor.CLOSED_HAND);
      dragDetected = true;

      lastValidCanvasX = getDragHorizontalValue(event);
      lastValidCanvasY = getDragVerticalValue(event);
      paint(lastValidCanvasX, lastValidCanvasY, false);
      notifyPositionChange();
    }
  }

  /**
   * Establish if the current horizontal drag event is valid or not.
   * If it's valid, it will return the X coordinate at which the canvas should be 'placed'.
   * @param event - current drag event
   * @return the new canvas X position if the event it's valid else it will return the previous valid position
   */
  protected int getDragHorizontalValue(MouseEvent event) {
    int x = (int) (event.getX() - pressedX);
    int newCanvasX = canvasX + x;

    //System.out.println("---horizontal x=" + x + "newX=" + newCanvasX + " ZOOM=" + CELL_WIDTH);
    if (getMapWidth() < getWidth())
      return (int) (getWidth() - getMapWidth()) / 2;

    if((newCanvasX >= 0 && newCanvasX <= CELL_WIDTH) ||
            (newCanvasX < 0 && ((newCanvasX + getMapWidth()) - getWidth()) >= -CELL_WIDTH))
      return newCanvasX;

    return lastValidCanvasX;
  }

  /**
   * Establish if the current vertical drag event is valid or not.
   * If it's valid, it will return the Y coordinate at which the canvas should be 'placed'.
   * @param event - current drag event
   * @return the new canvas Y position if the event it's valid else it will return the previous valid position
   */
  protected int getDragVerticalValue(MouseEvent event) {
    int y = (int) (event.getY() - pressedY);
    int newCanvasY = canvasY + y;

    //System.out.println("---vertical y=" + y + "newY=" + newCanvasY + " ZOOM=" + CELL_WIDTH);
    if (getMapHeight() < getHeight())
      return (int) (getHeight() - getMapHeight()) / 2;

    if((newCanvasY >= 0 && newCanvasY <= CELL_HEIGHT) ||
            (newCanvasY < 0 && ((newCanvasY + getMapHeight()) - getHeight()) >= -CELL_HEIGHT)) {
      return newCanvasY;
    }

    return lastValidCanvasY;
  }

  /**
   * Paint canvas content
   */
  public void paint() {
    paint(canvasX, canvasY, true);
  }

  /**
   * Paint canvas content
   * The virtual coordinates are a simulation of physically translating the canvas. The canvas position is actually
   *  static, in order to improve the rendering process performance.
   * @param canvasXPos - virtual canvas X position
   * @param canvasYPos - virtual canvas Y position
   *
   */
  protected void paint(int canvasXPos, int canvasYPos, boolean updateCoordinates) {
    int[] info = getMapInfoBasedOnItsPosition(canvasXPos, canvasYPos);
    /* update canvas position */
    if(updateCoordinates || info[FORCE_UPDATE] != 0) {
      if(info[START_X] > 0) {
        canvasX = info[START_X];
        lastValidCanvasX = canvasX;
      } else if(info[FORCE_UPDATE] == 1 || info[FORCE_UPDATE] == 3) {
        canvasX = info[CANVAS_X];
        lastValidCanvasX = canvasX;
      }

      if (info[START_Y] > 0) {
        canvasY = info[START_Y];
        lastValidCanvasY = canvasY;
      } else if(info[FORCE_UPDATE] == 2 || info[FORCE_UPDATE] == 3) {
        canvasY = info[CANVAS_Y];
        lastValidCanvasY = canvasY;
      }
    }
    updateMapModelDetails();

    paintContent(info[STOP_INDEX_Y], info[STOP_INDEX_X], info[START_INDEX_X], info[START_INDEX_Y], info[START_X], info[START_Y], info[STOP_X], info[STOP_Y]);
  }

  /**
   * Paint all the canvas content. This includes all the enabled layers and the grid.
   *
  //   * @param mapLayers - map layers
   * @param stopIndexY - number of rows in the matrix
   * @param stopIndexX - number of columns in the matrix
   * @param startIndexX - the horizontal index from which to take the tiles (horizontally)
   * @param startIndexY - the vertical index from which to take the tiles (vertically)
   * @param startX - the X coordinate from which the drawing will start
   * @param startY - the Y coordinate from which the drawing will start
   *
   */
  protected void paintContent(int stopIndexY, int stopIndexX, int startIndexX, int startIndexY, int startX, int startY, int stopX, int stopY) {
    double canvasWidth = getWidth();
    double canvasHeight = getHeight();

    GraphicsContext g = getGraphicsContext2D();
    g.setFill(fillColor);
    g.clearRect(0, 0, canvasWidth, canvasHeight);
    g.fillRect(startX, startY, stopX, stopY);

    CustomMap<LayerModel, MapTilesContainer.TilesMatrix> map = tilesContainer.getTilesMap();
    if (map != null) {
      int x;
      for (LayerModel layer : mapDetail.getLayers()) {
        if (!layer.isChecked())
          continue;
        int y = startY;
        MapTilesContainer.TilesMatrix tilesMatrix = map.get(layer);
        if (tilesMatrix != null) {
          ImageModel[][] images = tilesMatrix.getTilesMatrix();
          for (int i=startIndexY; i<=stopIndexY; i++) {
            x = startX;
            for (int j=startIndexX; j<=stopIndexX; j++) {
              ImageModel image = images[i][j];
              if (image != null)
                g.drawImage(image.getImage(), x, y, CELL_WIDTH, CELL_HEIGHT);

              x += CELL_WIDTH;
              if (x > canvasWidth)
                break;
            }
            y += CELL_HEIGHT;
            if (y > canvasHeight)
              break;
          }
        }
      }
    }

    if (gridEnabled)
      drawGrid(g, startX, startY, stopX, stopY);
  }

  /**
   * Draw the grid lines over the canvas
   *
   * @param g      - GraphicsContext
   * @param startX - the X coordinate from which the drawing will start
   * @param startY - the Y coordinate from which the drawing will start
   * @param stopX - the X coordinate at which the drawing will end
   * @param stopY - the Y coordinate at which the drawing will end
   */
  protected void drawGrid(GraphicsContext g, int startX, int startY, int stopX, int stopY) {
    if (!gridEnabled)
      return;
    g.setStroke(gridColor);
    g.setLineWidth(1.0);

    /* TO DO : review the logic for width and height variables (they can be removed); see the usage in
        verticalLineLength/horizontalLineLength !!! */
    int width;
    if(isHorizontalDraggable())
      width = startX >= 0 ? stopX + (startX != 0 ? CELL_WIDTH : 0) : stopX + startX;
    else
      width = COLUMNS * CELL_WIDTH;

    int height;
    if(isVerticalDraggable())
      height = startY >= 0 ? stopY + (startY != 0 ? CELL_HEIGHT : 0) : stopY + startY;
    else
      height = ROWS * CELL_HEIGHT;

    //System.out.println("X=" + startX + " Y=" + startY + " sX=" + stopX + " sY=" + stopY + " w=" + width + " h=" + height);

    int cellsHeight = ROWS * CELL_HEIGHT + startY;
    int cellsWidth = COLUMNS * CELL_WIDTH + startX;
    int verticalLineLength = isVerticalDraggable() ? (height) : cellsHeight;
    int horizontalLineLength = isHorizontalDraggable() ? (width) : cellsWidth;
    /*
     * we add 0.5 to the initial line position to make sure that the line will be exactly 1px wide
     * for a complete explanation see : http://mail.openjdk.java.net/pipermail/openjfx-dev/2015-April/016991.html
     */

    /* draw vertical lines */
    double sharp;
    int limit = isHorizontalDraggable() ? horizontalLineLength : cellsWidth;
    for (int i = startX; i <= limit; i += CELL_WIDTH) {
      sharp = i + 0.5;
      g.strokeLine(sharp, startY, sharp, verticalLineLength);
      if(isHorizontalDraggable() && i > stopX)
        break;
    }

    /* draw horizontal lines */
    limit = isVerticalDraggable() ? verticalLineLength : cellsHeight;
    for (int i = startY; i <= limit; i += CELL_HEIGHT) {
      sharp = i + 0.5;
      g.strokeLine(startX, sharp, horizontalLineLength, sharp);
      if(isVerticalDraggable() && i > stopY)
        break;
    }
  }

  /**
   * Draw the grid lines over the canvas plus it will color the cell border over which the mouse is.
   *
   * @param g      - GraphicsContext
   * @param x      - the x coordinate of the hovered cell
   * @param y      - the y coordinate of the hovered cell
   */
  protected void drawGrid(GraphicsContext g, int x, int y, int startX, int startY, int stopX, int stopY) {
    // draw the whole grid only if it's enabled
    if (gridEnabled)
      drawGrid(g, startX, startY, stopX, stopY);

    // draw selection based on the selectedDrawModel type
    g.setStroke(squareColor);
    if (selectedDrawModel == null || selectedDrawModel.getDrawModelType() == AbstractDrawModel.DrawModelType.TILE) {
      g.strokeRect(x + 0.5, y + 0.5, CELL_WIDTH, CELL_HEIGHT);
    } else if (selectedDrawModel.getDrawModelType() == AbstractDrawModel.DrawModelType.BRUSH) {
      int row = y / CELL_HEIGHT;
      int col = x / CELL_WIDTH;
      if (!checkMatrixBorders(row, col))
        return;

      g.strokeRect(x + 0.5, y + 0.5, CELL_WIDTH, CELL_HEIGHT);
      if (checkMatrixBorders(row - 1, col - 1))
        g.strokeRect(x - CELL_WIDTH + 0.5, y - CELL_HEIGHT + 0.5, CELL_WIDTH, CELL_HEIGHT);
      if (checkMatrixBorders(row - 1, col))
        g.strokeRect(x + 0.5, y - CELL_HEIGHT + 0.5, CELL_WIDTH, CELL_HEIGHT);
      if (checkMatrixBorders(row - 1, col + 1))
        g.strokeRect(x + CELL_WIDTH + 0.5, y - CELL_HEIGHT + 0.5, CELL_WIDTH, CELL_HEIGHT);
      if (checkMatrixBorders(row, col - 1))
        g.strokeRect(x - CELL_WIDTH + 0.5, y + 0.5, CELL_WIDTH, CELL_HEIGHT);
      if (checkMatrixBorders(row, col + 1))
        g.strokeRect(x + CELL_WIDTH + 0.5, y + 0.5, CELL_WIDTH, CELL_HEIGHT);
      if (checkMatrixBorders(row + 1, col - 1))
        g.strokeRect(x - CELL_WIDTH + 0.5, y + CELL_HEIGHT + 0.5, CELL_WIDTH, CELL_HEIGHT);
      if (checkMatrixBorders(row + 1, col))
        g.strokeRect(x + 0.5, y + CELL_HEIGHT + 0.5, CELL_WIDTH, CELL_HEIGHT);
      if (checkMatrixBorders(row + 1, col + 1))
        g.strokeRect(x + CELL_WIDTH + 0.5, y + CELL_HEIGHT + 0.5, CELL_WIDTH, CELL_HEIGHT);
    } else if (selectedDrawModel.getDrawModelType() == AbstractDrawModel.DrawModelType.OBJECT &&
            selectedDrawModel instanceof ObjectModel) {
      ObjectModel objectModel = (ObjectModel) selectedDrawModel;
      int rows = objectModel.getRows();
      int cols = objectModel.getCols();
      int selectedRow = y / CELL_HEIGHT;
      int selectedCol = x / CELL_WIDTH;
      int primaryX = objectModel.getPrimaryTileX();
      int primaryY = objectModel.getPrimaryTileY();

      int offsetY = y % CELL_HEIGHT;
      int offsetX = x % CELL_WIDTH;

      int gridStartRow = selectedRow - primaryY;
      int gridStartCol = selectedCol - primaryX;
      for (int row = gridStartRow; row < gridStartRow + rows; row ++) {
        for (int col = gridStartCol; col < gridStartCol + cols; col ++) {
          if (checkMatrixBorders(row, col))
            g.strokeRect(col * CELL_WIDTH + .5 + offsetX, row * CELL_HEIGHT + .5 + offsetY, CELL_WIDTH, CELL_HEIGHT);
        }
      }
    }
  }

  /**
   * Calculate the needed map information.
   * @param canvasXPos - map X coordinate
   * @param canvasYPos - map Y coordinate
   * @return a vector which contains all information that is useful in order to draw the map
   *         (information = positions in cartesian system or in the map matrix)
   */
  protected int[] getMapInfoBasedOnItsPosition(int canvasXPos, int canvasYPos) {
    boolean isHorizontalDraggable = isHorizontalDraggable();
    boolean isVerticalDraggable = isVerticalDraggable();

    int rows = ROWS;
    int columns = COLUMNS;
    int forceUpdate = 0;
    /* if the right margin is visible, it's value will be a negative integer */
    int rightMargin = isHorizontalDraggable ? getMapWidth() + canvasXPos - (int) getWidth() : 0;
    int bottomMargin = isVerticalDraggable ? getMapHeight() + canvasYPos - (int) getHeight() : 0;

    /* update the received map coordinates if the right margin to big */
    if(rightMargin < -CELL_WIDTH) {
      canvasXPos += -rightMargin - CELL_WIDTH;
      forceUpdate = 1;
    }
    if(bottomMargin < -CELL_HEIGHT) {
      canvasYPos += -bottomMargin - CELL_HEIGHT;
      forceUpdate = forceUpdate == 1 ? 3 : 2;
    }
    mapInfo[FORCE_UPDATE] = forceUpdate;

    /* from which position we should start drawing the matrix; they are matrix indexes */
    /* an index remains 0 when the canvas couldn't be dragged anymore (it reaches the maxim margin) */
    int startIndexX = 0;
    int startIndexY = 0;
    int stopIndexX;
    int stopIndexY;
    if(isHorizontalDraggable)
      startIndexX = canvasXPos == CELL_WIDTH ? 0 : ((canvasXPos < 0 ? (-1 * canvasXPos) : canvasXPos) / CELL_WIDTH);

    if(isVerticalDraggable)
      startIndexY = canvasYPos == CELL_HEIGHT ? 0 : ((canvasYPos < 0 ? (-1 * canvasYPos) : canvasYPos) / CELL_HEIGHT);

    /* from which coordinates we should start drawing the tiles; they are cartesian coordinates */
    /* if the canvas is not draggable on a dimension, it will be centered relative to that dimension */
    int startX, startY;
    if(isHorizontalDraggable)
      startX = canvasXPos < CELL_WIDTH ? canvasXPos % CELL_WIDTH : CELL_WIDTH;
    else
      startX = (int) (getWidth() - (columns * CELL_WIDTH)) / 2;
    stopIndexX = ((int) getWidth() - startX) / CELL_WIDTH + startIndexX;
    stopIndexX = stopIndexX >= columns ? columns - 1 : stopIndexX;

    if(isVerticalDraggable)
      startY = canvasYPos < CELL_HEIGHT ? canvasYPos % CELL_HEIGHT : CELL_HEIGHT;
    else
      startY = (int) (getHeight() - (rows * CELL_HEIGHT)) / 2;
    stopIndexY = ((int) getHeight() - startY) / CELL_HEIGHT + startIndexY;
    stopIndexY = stopIndexY >= rows ? rows - 1 : stopIndexY;

//    System.out.println("canvasX=" + canvasXPos + " canvasY=" + canvasYPos + " startIndexX=" + startIndexX + " startIndexY="
//        + startIndexY + " startX=" + startX + " startY=" + startY + " stopIndexX=" + stopIndexX + " stopIndexY=" + stopIndexY
//        + " ZOOM=" + CELL_WIDTH);

    int stopX = (stopIndexX - startIndexX + 1) * CELL_WIDTH;
    int stopY = (stopIndexY - startIndexY + 1) * CELL_HEIGHT;

    mapInfo[START_INDEX_X] = startIndexX;
    mapInfo[START_INDEX_Y] = startIndexY;
    mapInfo[STOP_INDEX_X] = stopIndexX;
    mapInfo[STOP_INDEX_Y] = stopIndexY;
    mapInfo[START_X] = startX;
    mapInfo[START_Y] = startY;
    mapInfo[STOP_X] = stopX;
    mapInfo[STOP_Y] = stopY;
    mapInfo[CANVAS_X] = canvasXPos;
    mapInfo[CANVAS_Y] = canvasYPos;

    return mapInfo;
  }

  protected boolean checkMatrixBorders(int row, int col) {
    return row >= 0 && col >= 0 && row < ROWS && col < COLUMNS;
  }

  /**
   * Check if the mouse was pressed in the painted area.
   * @return true, if the last mouse click coordinates are within map borders
   */
  protected boolean checkMouseBorders() {
    return checkMouseBorders((int) pressedX, (int) pressedY);
  }

  public boolean checkMouseBorders(int mouseX, int mouseY) {
    //System.out.println("check bounds : canvasX=" + canvasX + " canvasY=" + canvasY + " x=" + mouseX + " y=" + mouseY);
    return mouseX >= canvasX && mouseX <= getMapWidth() + canvasX &&
            mouseY >= canvasY && mouseY <= getMapHeight() + canvasY;
  }

  public int getMapWidth() {
    return COLUMNS * CELL_WIDTH;
  }

  public int getMapHeight() {
    return ROWS * CELL_HEIGHT;
  }

  /**
   * Check if the canvas can be dragged horizontally. It can not be dragged if its width is smaller than the view port width.
   *
   * @return true - if it's draggable
   * false - otherwise
   */
  public boolean isHorizontalDraggable() {
    return COLUMNS * CELL_WIDTH > getWidth();
  }

  /**
   * Check if the canvas can be dragged vertically. It can not be dragged if its height is smaller than the view port height.
   *
   * @return true - if it's draggable
   * false - otherwise
   */
  public boolean isVerticalDraggable() {
    return ROWS * CELL_HEIGHT > getHeight();
  }

  public void setEnableGrid(boolean value) {
    gridEnabled = value;
    if (mapDetail != null)
      mapDetail.setShowGrid(value);
  }

  public void notifyPositionChange() {
//    if(notificationType != MapNotificationTypes.NONE)
//      controller.handleChangePosition(notificationType);
  }

  public MapTilesContainer getTilesContainer() {
    return tilesContainer;
  }

  public void updateMapModelDetails() {
    if (mapDetail != null) {
      mapDetail.setX(canvasX);
      mapDetail.setY(canvasY);
      mapDetail.setZoomStatus(zoomStatus);
    }
  }

  public void updateMapModelInfos() {
    mapDetail.setMapTilesInfo(tilesContainer.getMapInfo(mapDetail.getLayers()));
  }

  public void setDrawingEntity(AbstractDrawModel drawModel) {
    selectedDrawModel = drawModel;
  }

  public MapDetail getMapDetail() {
    updateMapModelDetails();
    return mapDetail;
  }

  /**
   * Needs to be overwritten in the canvas that used as a mini map
   * @param x - x coordinate
   * @param y - y coordinate
   * @param w - width of the visible area
   * @param h - height of the visible area
   */
  public void updateVisibleArea(int x, int y, int w, int h) {}

  public int getCanvasX() { return dragDetected ? lastValidCanvasX : canvasX; }

  public int getCanvasY() { return dragDetected ? lastValidCanvasY : canvasY; }

  public int getHoveredCellX() {
    return hoveredCellX;
  }

  public int getHoveredCellY() {
    return hoveredCellY;
  }

  public int getCellWidth() { return CELL_WIDTH; }

  public int getCellHeight() { return CELL_HEIGHT; }

  public int getStartX() { return mapInfo[START_X]; }

  public int getStartY() { return mapInfo[START_Y]; }

  public int getStartIndexX() { return mapInfo[START_INDEX_X]; }

  public int getStartIndexY() { return mapInfo[START_INDEX_Y]; }

  public boolean isDragDetected() { return  dragDetected; }

  @Override
  public boolean isResizable() {
    return true;
  }
}

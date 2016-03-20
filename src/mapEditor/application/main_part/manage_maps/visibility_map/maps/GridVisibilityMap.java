package mapEditor.application.main_part.manage_maps.visibility_map.maps;

import javafx.scene.input.MouseEvent;
import mapEditor.application.main_part.app_utils.models.MapDetail;
import mapEditor.application.main_part.manage_maps.MapCanvas;

/**
 *
 * Created by razvanolar on 19.03.2016.
 */
public class GridVisibilityMap extends MapCanvas {

  public GridVisibilityMap(MapDetail mapDetail) {
    super(mapDetail.getRows(), mapDetail.getColumns());
    this.mapDetail = mapDetail;
    this.fillColor = mapDetail.getBackgroundColor();
    this.gridColor = mapDetail.getGridColor();
    this.squareColor = mapDetail.getSquareColor();
    this.zoomStatus = mapDetail.getZoomStatus();
    this.gridEnabled = true;

    canvasX = mapDetail.getX();
    canvasY = mapDetail.getY();
    CELL_WIDTH = DEFAULT_CELL_WIDTH + zoomStatus;
    CELL_HEIGHT = DEFAULT_CELL_HEIGHT + zoomStatus;
  }

  public void onMouseDragged(MouseEvent event) {
    super.handleMouseDragEvent(event);
  }

  public void onMousePressed(MouseEvent event) {
    super.handleMousePressEvent(event);
  }

  public void onMouseMoved(MouseEvent event) {
    /* the cartesian coordinates of the hovered cell */
    int x = (int) event.getX() + 1;
    int y = (int) event.getY() + 1;

    if (!checkMouseBorders(x, y))
      return;

    hoveredCellX = (int) ((event.getX() - canvasX) / CELL_WIDTH) * CELL_WIDTH;
    hoveredCellY = (int) ((event.getY() - canvasY) / CELL_HEIGHT) * CELL_HEIGHT;
    paint();
  }

  public void onMouseReleased(MouseEvent event) {
    super.handleMouseReleaseEvent(event, false);
  }
}

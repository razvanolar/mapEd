package mapEditor.application.main_part.manage_maps.visibility_map.maps;

import javafx.scene.input.MouseEvent;
import mapEditor.application.main_part.app_utils.models.MapDetail;
import mapEditor.application.main_part.manage_maps.MapCanvas;

/**
 *
 * Created by razvanolar on 14.03.2016.
 */
public class ShadowMap extends MapCanvas {

  public ShadowMap(MapDetail mapDetail) {
    super(mapDetail.getRows(), mapDetail.getColumns());
    this.mapDetail = mapDetail;
    this.fillColor = mapDetail.getBackgroundColor();
    this.gridColor = mapDetail.getGridColor();
    this.squareColor = mapDetail.getSquareColor();
    this.zoomStatus = mapDetail.getZoomStatus();
    this.gridEnabled = false;

    canvasX = mapDetail.getX();
    canvasY = mapDetail.getY();
    CELL_WIDTH = DEFAULT_CELL_WIDTH + zoomStatus;
    CELL_HEIGHT = DEFAULT_CELL_HEIGHT + zoomStatus;
  }

  @Override
  public void paint() {
    super.paint();
  }

  public void onMouseDragged(MouseEvent event) {
    super.handleMouseDragEvent(event);
  }

  public void onMousePressed(MouseEvent event) {
    super.handleMousePressEvent(event);
  }

  public void onMouseReleased(MouseEvent event) {
    super.handleMouseReleaseEvent(event, false);
  }
}

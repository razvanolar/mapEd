package mapEditor.application.main_part.manage_maps.primary_map;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import mapEditor.application.main_part.manage_maps.primary_map.context_menu.PrimaryMapContextMenuController;
import mapEditor.application.main_part.manage_maps.utils.MapStoredImagesLoader;
import mapEditor.application.main_part.types.Controller;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class PrimaryMapController implements Controller {

  private PrimaryMapView canvas;
  private ScrollPane scrollPane; //canvas container
  private PrimaryMapContextMenuController contextMenuController;

  public PrimaryMapController(PrimaryMapView canvas, ScrollPane scrollPane, PrimaryMapContextMenuController contextMenuController) {
    this.canvas = canvas;
    this.scrollPane = scrollPane;
    this.contextMenuController = contextMenuController;
  }

  @Override
  public void bind() {
    MapStoredImagesLoader.createTilesContainerFromFromDiskIndexTiles(canvas);
    canvas.updateMapModelInfos();
    scrollPane.setContextMenu(contextMenuController.getContextMenu());
    addListeners();
    canvas.paint();
  }

  private void addListeners() {
    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
    canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> canvas.onMouseReleasedEvent(event, true));
    canvas.addEventHandler(MouseEvent.MOUSE_MOVED, canvas::onMouseMovedEvent);
    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, canvas::onMouseDraggedEvent);

    scrollPane.addEventFilter(ScrollEvent.SCROLL, canvas::onScrollEvent);
  }

  private void onMousePressed(MouseEvent event) {
    canvas.onMousePressedEvent(event);
  }
}

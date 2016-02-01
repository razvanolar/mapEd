package mapEditor.application.manage_maps.primary_map;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import mapEditor.application.types.Controller;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class PrimaryMapController implements Controller {

  private PrimaryMapView canvas;
  private ScrollPane scrollPane; //canvas container

  public PrimaryMapController(PrimaryMapView canvas, ScrollPane scrollPane) {
    this.canvas = canvas;
    this.scrollPane = scrollPane;
  }

  @Override
  public void bind() {
    addListeners();
  }

  private void addListeners() {
    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, canvas::onMousePressedEvent);
    canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> canvas.onMouseReleasedEvent(event, true));
    canvas.addEventHandler(MouseEvent.MOUSE_MOVED, canvas::onMouseMovedEvent);
    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, canvas::onMouseDraggedEvent);

    scrollPane.addEventFilter(ScrollEvent.SCROLL, canvas::onScrollEvent);
  }
}

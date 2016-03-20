package mapEditor.application.main_part.manage_maps.visibility_map;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.app_utils.constants.CssConstants;

/**
 *
 * Created by razvanolar on 14.03.2016.
 */
public class Map2DVisibilityView implements Map2DVisibilityController.IMap2DVisibilityView {

  private ScrollPane scrollPane;

  public Map2DVisibilityView() {
    initGUI();
  }

  private void initGUI() {
    scrollPane = new ScrollPane();
    scrollPane.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);
  }

  public ScrollPane getScrollPane() {
    return scrollPane;
  }

  @Override
  public Region asNode() {
    return scrollPane;
  }
}

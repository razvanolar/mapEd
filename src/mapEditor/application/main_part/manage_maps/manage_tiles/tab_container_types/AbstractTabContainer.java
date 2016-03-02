package mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types;

import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.manage_maps.utils.TabType;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 01.03.2016.
 */
public abstract class AbstractTabContainer implements View {

  protected TabType tabType;

  public TabType getTabType() {
    return tabType;
  }

  public abstract ImageModel getSelectedTile();
}

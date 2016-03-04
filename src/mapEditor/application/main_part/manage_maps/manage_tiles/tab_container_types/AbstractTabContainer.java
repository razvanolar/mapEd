package mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types;

import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.TabKey;
import mapEditor.application.main_part.manage_maps.utils.TabType;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 01.03.2016.
 */
public abstract class AbstractTabContainer implements View {

  protected String name;
  protected TabType tabType;
  protected TabKey key;

  public String getName() {
    return name;
  }

  public TabType getTabType() {
    return tabType;
  }

  public TabKey getKey() {
    if (key == null)
      key = new TabKey(name, tabType);
    return key;
  }

  public abstract ImageModel getSelectedTile();
}

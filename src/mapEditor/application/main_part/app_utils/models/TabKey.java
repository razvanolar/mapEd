package mapEditor.application.main_part.app_utils.models;

import mapEditor.application.main_part.manage_maps.utils.TabType;

/**
 *
 * Created by razvanolar on 04.03.2016.
 */
public class TabKey {

  private String name;
  private TabType type;

  public TabKey(String name, TabType type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public TabType getType() {
    return type;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof TabKey))
      return false;
    TabKey key = (TabKey) obj;
    return this.name.equals(key.name) && this.type == key.type;
  }
}

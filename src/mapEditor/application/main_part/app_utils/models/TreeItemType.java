package mapEditor.application.main_part.app_utils.models;

/**
 * Used in LazyTreeItem to indicate if a node have a special meaning or not.
 *
 * Created by razvanolar on 10.02.2016.
 */
public enum  TreeItemType {
  NORMAL(1), FOLDER(1), IMAGE(1),
  PROJECT_TILES_GROUP_FOLDER(0),
  PROJECT_TILE_SETS_FOLDER(0),
  PROJECT_TILES_FOLDER(0),
  PROJECT_CHARACTERS_FOLDER(0),
  PROJECT_MAPS_FOLDER(0);

  int type;
  TreeItemType(int type) {
    this.type = type;
  }

  public boolean isSystemType() {
    return type == 0;
  }
}

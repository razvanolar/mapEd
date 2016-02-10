package mapEditor.application.main_part.app_utils.models;

/**
 * Used in LazyTreeItem to indicate if a node have a special meaning or not.
 *
 * Created by razvanolar on 10.02.2016.
 */
public enum  TreeItemType {

  NORMAL(0), PROJECT_FOLDER(1);

  int type;
  TreeItemType(int type) {
    this.type = type;
  }

  public boolean isNormalType() {
    return this.type == 0;
  }
}

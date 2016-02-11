package mapEditor.application.main_part.project_tree.context_menu;

import javafx.scene.control.ContextMenu;
import mapEditor.application.main_part.app_utils.models.LazyTreeItem;
import mapEditor.application.main_part.app_utils.models.TreeItemType;
import mapEditor.application.main_part.types.Controller;

/**
 *
 * Created by razvanolar on 11.02.2016.
 */
public class ProjectTreeContextMenuController implements Controller {

  public interface IProjectTreeContextMenuView {
    void setState(TreeItemType treeItemType, TreeItemType parentItemType);
    ContextMenu getContextMenu();
  }

  private IProjectTreeContextMenuView view;
  private LazyTreeItem selectedItem;
  private LazyTreeItem parentItem;

  public ProjectTreeContextMenuController(IProjectTreeContextMenuView view) {
    this.view = view;
  }

  @Override
  public void bind() {

  }

  private LazyTreeItem getParentForSelectedItem() {
    if (selectedItem == null || selectedItem.getType().isSystemType())
      return null;
    LazyTreeItem parent = (LazyTreeItem) selectedItem.getParent();
    while (parent != null && !parent.getType().isSystemType())
      parent = (LazyTreeItem) parent.getParent();
    return parent;
  }

  public ContextMenu getContextMenu() {
    return view.getContextMenu();
  }

  public void setSelectedItem(LazyTreeItem item) {
    selectedItem = item;
    parentItem = getParentForSelectedItem();
    view.setState(selectedItem != null ? selectedItem.getType() : null, parentItem != null ? parentItem.getType() : null);
  }
}

package mapEditor.application.main_part.project_tree.context_menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import mapEditor.application.main_part.app_utils.models.LazyTreeItem;
import mapEditor.application.main_part.app_utils.models.TreeItemType;
import mapEditor.application.main_part.project_tree.utils.ProjectTreeContextMenuListener;
import mapEditor.application.main_part.types.Controller;

/**
 *
 * Created by razvanolar on 11.02.2016.
 */
public class ProjectTreeContextMenuController implements Controller {

  public interface IProjectTreeContextMenuView {
    void setState(TreeItemType treeItemType, TreeItemType parentItemType);
    MenuItem getNewDirectoryMenuItem();
    MenuItem getNewTileSetsMenuItem();
    MenuItem getOpenMapMenuItem();
    MenuItem getOpenInImageEditorMenuItem();
    MenuItem getOpenTilesInNewTabMenuItem();
    MenuItem getOpenTilesInExistingTabMenuItem();
    MenuItem getOpenTilesInImageEditor();
    MenuItem getExportToHtmlMenuItem();
    ContextMenu getContextMenu();
  }

  private IProjectTreeContextMenuView view;
  private ProjectTreeContextMenuListener listener;
  private LazyTreeItem selectedItem;
  private LazyTreeItem parentItem;

  public ProjectTreeContextMenuController(IProjectTreeContextMenuView view, ProjectTreeContextMenuListener listener) {
    this.view = view;
    this.listener = listener;
  }

  @Override
  public void bind() {
    addListeners();
  }

  private void addListeners() {
    view.getNewDirectoryMenuItem().setOnAction(event -> listener.createNewDirectory());

    view.getNewTileSetsMenuItem().setOnAction(event -> listener.loadNewTileSets());

    view.getOpenMapMenuItem().setOnAction(event -> listener.openMap());

    view.getOpenInImageEditorMenuItem().setOnAction(event -> listener.openInImageEditor());

    view.getOpenTilesInNewTabMenuItem().setOnAction(event -> listener.openTilesInNewTab());

    view.getOpenTilesInImageEditor().setOnAction(event1 -> listener.openTilesInImageEditor());

    view.getExportToHtmlMenuItem().setOnAction(event -> listener.exportMapToHtml());
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

  public LazyTreeItem getSelectedItem() {
    return selectedItem;
  }

  public LazyTreeItem getParentItem() {
    return parentItem;
  }
}

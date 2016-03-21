package mapEditor.application.main_part.manage_maps.primary_map.context_menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 *
 * Created by razvanolar on 21.03.2016.
 */
public class PrimaryMapContextMenuView implements PrimaryMapContextMenuController.IPrimaryMapContextMenuView {

  private ContextMenu contextMenu;
  private MenuItem saveMenuItem;
  private MenuItem renameMenuItem;
  private MenuItem deleteMenuItem;
  private MenuItem copyPathMenuItem;
  private MenuItem exportToHtmlMenuItem;
  private MenuItem changeAttributesMenuItem;

  public PrimaryMapContextMenuView() {
    initGUI();
  }

  private void initGUI() {
    saveMenuItem = new MenuItem("Save");
    renameMenuItem = new MenuItem("Rename");
    deleteMenuItem = new MenuItem("Delete");
    copyPathMenuItem = new MenuItem("Copy Path");
    exportToHtmlMenuItem = new MenuItem("Export to HTML...");
    changeAttributesMenuItem = new MenuItem("Change Attributes");
    contextMenu = new ContextMenu(saveMenuItem,
            new SeparatorMenuItem(),
            renameMenuItem,
            deleteMenuItem,
            new SeparatorMenuItem(),
            copyPathMenuItem,
            new SeparatorMenuItem(),
            exportToHtmlMenuItem,
            changeAttributesMenuItem);
  }

  public MenuItem getSaveMenuItem() {
    return saveMenuItem;
  }

  public MenuItem getRenameMenuItem() {
    return renameMenuItem;
  }

  public MenuItem getDeleteMenuItem() {
    return deleteMenuItem;
  }

  public MenuItem getCopyPathMenuItem() {
    return copyPathMenuItem;
  }

  public MenuItem getExportToHtmlMenuItem() {
    return exportToHtmlMenuItem;
  }

  public MenuItem getChangeAttributesMenuItem() {
    return changeAttributesMenuItem;
  }

  @Override
  public ContextMenu getContextMenu() {
    return contextMenu;
  }
}

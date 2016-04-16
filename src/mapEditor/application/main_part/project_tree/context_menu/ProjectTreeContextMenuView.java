package mapEditor.application.main_part.project_tree.context_menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import mapEditor.application.main_part.app_utils.models.TreeItemType;

/**
 *
 * Created by razvanolar on 11.02.2016.
 */
public class ProjectTreeContextMenuView implements ProjectTreeContextMenuController.IProjectTreeContextMenuView {

  private ContextMenu contextMenu;

  private Menu newMenu;
  private MenuItem newDirectoryMenuItem;
  private MenuItem newTileSetsMenuItem;
  private MenuItem newTilesMenuItem;
  private MenuItem newMapMenuItem;
  private MenuItem openMapMenuItem;
  private Menu openTilesMenu;
  private MenuItem openTilesInNewTabMenuItem;
  private MenuItem openTilesInExistingTabMenuItem;
  private MenuItem openTilesInImageEditor;
  private MenuItem openInImageEditorMenuItem;
  private MenuItem useForDrawingMenuItem;
  private MenuItem renameMenuItem;
  private MenuItem deleteMenuItem;
  private MenuItem clearMenuItem;
  private MenuItem copyPathMenuItem;
  private MenuItem exportToHtmlMenuItem;

  public ProjectTreeContextMenuView() {
    initGUI();
  }

  private void initGUI() {
    newMenu = new Menu("New");
    newDirectoryMenuItem = new MenuItem("Directory");
    newTileSetsMenuItem = new MenuItem("Tile Sets");
    newTilesMenuItem = new MenuItem("Tiles");
    newMapMenuItem = new MenuItem("Map");
    openMapMenuItem = new MenuItem("Open Map");
    openTilesMenu = new Menu("Open Tiles");
    openTilesInNewTabMenuItem = new MenuItem("In New Tab");
    openTilesInExistingTabMenuItem = new MenuItem("In Existing Tab");
    openTilesInImageEditor = new MenuItem("In Image Editor");
    openInImageEditorMenuItem = new MenuItem("Open in Image Editor");
    useForDrawingMenuItem = new MenuItem("Use For Drawing");
    renameMenuItem = new MenuItem("Rename");
    deleteMenuItem = new MenuItem("Delete");
    clearMenuItem = new MenuItem("Clear");
    copyPathMenuItem = new MenuItem("Copy Path");
    exportToHtmlMenuItem = new MenuItem("Export to HTML...");
    contextMenu = new ContextMenu();

    newMenu.getItems().addAll(newDirectoryMenuItem,
            newTileSetsMenuItem,
            newTilesMenuItem,
            newMapMenuItem);

    openTilesMenu.getItems().addAll(openTilesInNewTabMenuItem,
            openTilesInExistingTabMenuItem,
            openTilesInImageEditor);

    contextMenu.getItems().addAll(newMenu,
            new SeparatorMenuItem(),
            openMapMenuItem,
            openTilesMenu,
            openInImageEditorMenuItem,
            useForDrawingMenuItem,
            new SeparatorMenuItem(),
            renameMenuItem,
            deleteMenuItem,
            clearMenuItem,
            new SeparatorMenuItem(),
            copyPathMenuItem,
            new SeparatorMenuItem(),
            exportToHtmlMenuItem);
  }

  @Override
  public void setState(TreeItemType treeItemType, TreeItemType parentItemType) {
    if (treeItemType == null) {
      enableAllItems(false);
      return;
    }
    enableAllItems(true);
    openTilesMenu.setDisable(true);
    exportToHtmlMenuItem.setDisable(true);
    switch (treeItemType) {
      case NORMAL:
        newMenu.setDisable(true);
        clearMenuItem.setDisable(true);
        openMapMenuItem.setDisable(true);
        openTilesMenu.setDisable(true);
        openInImageEditorMenuItem.setDisable(true);
        useForDrawingMenuItem.setDisable(true);
        break;
      case IMAGE:
        openMapMenuItem.setDisable(true);
        openTilesMenu.setDisable(true);
        newMenu.setDisable(true);
        clearMenuItem.setDisable(true);
        break;
      case MAP:
        newMenu.setDisable(true);
        clearMenuItem.setDisable(true);
        openTilesMenu.setDisable(true);
        openInImageEditorMenuItem.setDisable(true);
        useForDrawingMenuItem.setDisable(true);
        exportToHtmlMenuItem.setDisable(false);
        break;
      case FOLDER:
        switch (parentItemType) {
          case PROJECT_TILE_SETS_FOLDER:
            newTilesMenuItem.setDisable(true);
            newMapMenuItem.setDisable(true);
            break;
          case PROJECT_TILES_FOLDER:
            openTilesMenu.setDisable(false);
            openTilesMenu.setText("Open Tiles");
            newTileSetsMenuItem.setDisable(true);
            newMapMenuItem.setDisable(true);
            break;
          case PROJECT_BRUSHES_FOLDER:
            openTilesMenu.setText("Open Brushes");
            break;
          case PROJECT_CHARACTERS_FOLDER:
            newTileSetsMenuItem.setDisable(true);
            newTilesMenuItem.setDisable(true);
            newMapMenuItem.setDisable(true);
            break;
          case PROJECT_MAPS_FOLDER:
            newTileSetsMenuItem.setDisable(true);
            newTilesMenuItem.setDisable(true);
            break;
        }
        enableOpenMenuItems(false);
        break;
      case PROJECT_TILES_GROUP_FOLDER:
        newMenu.setDisable(true);
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        clearMenuItem.setDisable(true);
        enableOpenMenuItems(false);
        break;
      case PROJECT_TILE_SETS_FOLDER:
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        newTilesMenuItem.setDisable(true);
        newMapMenuItem.setDisable(true);
        enableOpenMenuItems(false);
        break;
      case PROJECT_TILES_FOLDER:
        openTilesMenu.setText("Open Tiles");
        openTilesMenu.setDisable(false);
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        newTileSetsMenuItem.setDisable(true);
        newMapMenuItem.setDisable(true);
        enableOpenMenuItems(false);
        break;
      case PROJECT_BRUSHES_FOLDER:
        openTilesMenu.setText("Open Brushes");
        openTilesMenu.setDisable(false);
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        newTileSetsMenuItem.setDisable(true);
        newTilesMenuItem.setDisable(true);
        newMapMenuItem.setDisable(true);
        enableOpenMenuItems(false);
        break;
      case PROJECT_CHARACTERS_FOLDER:
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        newTileSetsMenuItem.setDisable(true);
        newTilesMenuItem.setDisable(true);
        newMapMenuItem.setDisable(true);
        enableOpenMenuItems(false);
        break;
      case PROJECT_MAPS_FOLDER:
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        newTileSetsMenuItem.setDisable(true);
        newTilesMenuItem.setDisable(true);
        enableOpenMenuItems(false);
        break;
    }
  }

  private void enableAllItems(boolean value) {
    newMenu.setDisable(!value);
    newDirectoryMenuItem.setDisable(!value);
    newTileSetsMenuItem.setDisable(!value);
    newTilesMenuItem.setDisable(!value);
    newMapMenuItem.setDisable(!value);
    openMapMenuItem.setDisable(!value);
    openInImageEditorMenuItem.setDisable(!value);
    useForDrawingMenuItem.setDisable(!value);
    renameMenuItem.setDisable(!value);
    deleteMenuItem.setDisable(!value);
    clearMenuItem.setDisable(!value);
    copyPathMenuItem.setDisable(!value);
    exportToHtmlMenuItem.setDisable(!value);
  }

  private void enableOpenMenuItems(boolean value) {
    openMapMenuItem.setDisable(!value);
    openInImageEditorMenuItem.setDisable(!value);
    useForDrawingMenuItem.setDisable(!value);
  }

  public MenuItem getNewDirectoryMenuItem() {
    return newDirectoryMenuItem;
  }

  public MenuItem getNewTileSetsMenuItem() {
    return newTileSetsMenuItem;
  }

  public MenuItem getOpenMapMenuItem() {
    return openMapMenuItem;
  }

  public MenuItem getOpenTilesInNewTabMenuItem() {
    return openTilesInNewTabMenuItem;
  }

  public MenuItem getOpenTilesInExistingTabMenuItem() {
    return openTilesInExistingTabMenuItem;
  }

  public MenuItem getOpenTilesInImageEditor() {
    return openTilesInImageEditor;
  }

  public MenuItem getOpenInImageEditorMenuItem() {
    return openInImageEditorMenuItem;
  }

  public MenuItem getExportToHtmlMenuItem() {
    return exportToHtmlMenuItem;
  }

  public ContextMenu getContextMenu() {
    return contextMenu;
  }
}

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
  private MenuItem newCharactersMenuItem;
  private MenuItem newMapMenuItem;
  private MenuItem openInImageEditorMenuItem;
  private MenuItem useForDrawingMenuItem;
  private MenuItem renameMenuItem;
  private MenuItem deleteMenuItem;
  private MenuItem clearMenuItem;
  private MenuItem copyPathMenuItem;

  public ProjectTreeContextMenuView() {
    initGUI();
  }

  private void initGUI() {
    newMenu = new Menu("New");
    newDirectoryMenuItem = new MenuItem("Directory");
    newTileSetsMenuItem = new MenuItem("Tile Sets");
    newTilesMenuItem = new MenuItem("Tiles");
    newCharactersMenuItem = new MenuItem("Characters");
    newMapMenuItem = new MenuItem("Map");
    openInImageEditorMenuItem = new MenuItem("Open in Image Editor");
    useForDrawingMenuItem = new MenuItem("Use For Drawing");
    renameMenuItem = new MenuItem("Rename");
    deleteMenuItem = new MenuItem("Delete");
    clearMenuItem = new MenuItem("Clear");
    copyPathMenuItem = new MenuItem("Copy Path");
    contextMenu = new ContextMenu();

    newMenu.getItems().addAll(newDirectoryMenuItem,
            newTileSetsMenuItem,
            newTilesMenuItem,
            newCharactersMenuItem,
            newMapMenuItem);

    contextMenu.getItems().addAll(newMenu,
            new SeparatorMenuItem(),
            openInImageEditorMenuItem,
            useForDrawingMenuItem,
            new SeparatorMenuItem(),
            renameMenuItem,
            deleteMenuItem,
            clearMenuItem,
            new SeparatorMenuItem(),
            copyPathMenuItem);
  }

  @Override
  public void setState(TreeItemType treeItemType, TreeItemType parentItemType) {
    if (treeItemType == null) {
      enableAllItems(false);
      return;
    }
    enableAllItems(true);
    switch (treeItemType) {
      case NORMAL:
        newMenu.setDisable(true);
        clearMenuItem.setDisable(true);
        openInImageEditorMenuItem.setDisable(true);
        useForDrawingMenuItem.setDisable(true);
        break;
      case IMAGE:
        newMenu.setDisable(true);
        clearMenuItem.setDisable(true);
        break;
      case FOLDER:
        switch (parentItemType) {
          case PROJECT_TILE_SETS_FOLDER:
            newTilesMenuItem.setDisable(true);
            newCharactersMenuItem.setDisable(true);
            newMapMenuItem.setDisable(true);
            break;
          case PROJECT_TILES_FOLDER:
            newTileSetsMenuItem.setDisable(true);
            newCharactersMenuItem.setDisable(true);
            newMapMenuItem.setDisable(true);
            break;
          case PROJECT_CHARACTERS_FOLDER:
            newTileSetsMenuItem.setDisable(true);
            newTilesMenuItem.setDisable(true);
            newMapMenuItem.setDisable(true);
            break;
          case PROJECT_MAPS_FOLDER:
            newTileSetsMenuItem.setDisable(true);
            newTilesMenuItem.setDisable(true);
            newCharactersMenuItem.setDisable(true);
            break;
        }
        break;
      case PROJECT_TILES_GROUP_FOLDER:
        newMenu.setDisable(true);
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        clearMenuItem.setDisable(true);
        break;
      case PROJECT_TILE_SETS_FOLDER:
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        newTilesMenuItem.setDisable(true);
        newCharactersMenuItem.setDisable(true);
        newMapMenuItem.setDisable(true);
        break;
      case PROJECT_TILES_FOLDER:
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        newTileSetsMenuItem.setDisable(true);
        newCharactersMenuItem.setDisable(true);
        newMapMenuItem.setDisable(true);
        break;
      case PROJECT_CHARACTERS_FOLDER:
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        newTileSetsMenuItem.setDisable(true);
        newTilesMenuItem.setDisable(true);
        newMapMenuItem.setDisable(true);
        break;
      case PROJECT_MAPS_FOLDER:
        renameMenuItem.setDisable(true);
        deleteMenuItem.setDisable(true);
        newTileSetsMenuItem.setDisable(true);
        newTilesMenuItem.setDisable(true);
        newCharactersMenuItem.setDisable(true);
        break;
    }
  }

  private void enableAllItems(boolean value) {
    newMenu.setDisable(!value);
    newDirectoryMenuItem.setDisable(!value);
    newTileSetsMenuItem.setDisable(!value);
    newTilesMenuItem.setDisable(!value);
    newCharactersMenuItem.setDisable(!value);
    newMapMenuItem.setDisable(!value);
    renameMenuItem.setDisable(!value);
    deleteMenuItem.setDisable(!value);
    clearMenuItem.setDisable(!value);
    copyPathMenuItem.setDisable(!value);
  }

  public MenuItem getOpenInImageEditorMenuItem() {
    return openInImageEditorMenuItem;
  }

  public ContextMenu getContextMenu() {
    return contextMenu;
  }
}

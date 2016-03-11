package mapEditor.application.main_part.manage_images.manage_tile_sets.cropped_tiles.simple_view.context_menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 *
 * Created by razvanolar on 17.02.2016.
 */
public class SimpleCroppedTileContextMenuView implements SimpleCroppedTileContextMenuController.ISimpleCroppedTileContextMenuView {

  private ContextMenu contextMenu;

  private MenuItem setNameMenuItem;
  private MenuItem setPathMenuItem;
  private MenuItem saveMenuItem;
  private MenuItem dropMenuItem;

  public SimpleCroppedTileContextMenuView() {
    initGUI();
  }

  private void initGUI() {
    setNameMenuItem = new MenuItem("Set Name");
    setPathMenuItem = new MenuItem("Set Path");
    saveMenuItem = new MenuItem("Save");
    dropMenuItem = new MenuItem("Drop");
    contextMenu = new ContextMenu(setNameMenuItem, setPathMenuItem, new SeparatorMenuItem(), saveMenuItem, dropMenuItem);
  }

  public MenuItem getSetNameMenuItem() {
    return setNameMenuItem;
  }

  public MenuItem getSetPathMenuItem() {
    return setPathMenuItem;
  }

  public MenuItem getSaveMenuItem() {
    return saveMenuItem;
  }

  public MenuItem getDropMenuItem() {
    return dropMenuItem;
  }

  @Override
  public ContextMenu getContextMenu() {
    return contextMenu;
  }
}

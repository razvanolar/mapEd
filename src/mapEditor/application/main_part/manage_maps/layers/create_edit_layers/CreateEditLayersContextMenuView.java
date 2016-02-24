package mapEditor.application.main_part.manage_maps.layers.create_edit_layers;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 *
 * Created by razvanolar on 24.02.2016.
 */
public class CreateEditLayersContextMenuView implements CreateEditLayersContextMenuController.ICreateEditLayersContextMenuView {

  private ContextMenu contextMenu;
  private MenuItem editLayerMenuItem;
  private MenuItem deleteLayerMenuItem;
  private MenuItem moveLayerUpMenuItem;
  private MenuItem moveLayerDownMenuItem;

  public CreateEditLayersContextMenuView() {
    initGUI();
  }

  private void initGUI() {
    editLayerMenuItem = new MenuItem("Edit");
    deleteLayerMenuItem = new MenuItem("Delete");
    moveLayerUpMenuItem = new MenuItem("Move Up");
    moveLayerDownMenuItem = new MenuItem("Move Down");
    contextMenu = new ContextMenu(editLayerMenuItem, deleteLayerMenuItem, new SeparatorMenuItem(), moveLayerUpMenuItem, moveLayerDownMenuItem);
  }

  public MenuItem getEditLayerMenuItem() {
    return editLayerMenuItem;
  }

  public MenuItem getDeleteLayerMenuItem() {
    return deleteLayerMenuItem;
  }

  public MenuItem getMoveLayerUpMenuItem() {
    return moveLayerUpMenuItem;
  }

  public MenuItem getMoveLayerDownMenuItem() {
    return moveLayerDownMenuItem;
  }

  public ContextMenu getContextMenu() {
    return contextMenu;
  }
}

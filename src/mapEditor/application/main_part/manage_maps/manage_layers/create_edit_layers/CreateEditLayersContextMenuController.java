package mapEditor.application.main_part.manage_maps.manage_layers.create_edit_layers;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import mapEditor.application.main_part.manage_maps.utils.SelectableLayerListener;
import mapEditor.application.main_part.types.Controller;

/**
 *
 * Created by razvanolar on 24.02.2016.
 */
public class CreateEditLayersContextMenuController implements Controller {

  public interface ICreateEditLayersContextMenuView {
    MenuItem getEditLayerMenuItem();
    MenuItem getDeleteLayerMenuItem();
    MenuItem getMoveLayerUpMenuItem();
    MenuItem getMoveLayerDownMenuItem();
    ContextMenu getContextMenu();
  }

  private ICreateEditLayersContextMenuView view;
  private SelectableLayerListener listener;

  public CreateEditLayersContextMenuController(ICreateEditLayersContextMenuView view, SelectableLayerListener listener) {
    this.view = view;
    this.listener = listener;
  }

  @Override
  public void bind() {
    view.getEditLayerMenuItem().setOnAction(event -> listener.onEditLayerButtonSelection());
    view.getDeleteLayerMenuItem().setOnAction(event -> listener.onDeleteLayerButtonSelection());
    view.getMoveLayerUpMenuItem().setOnAction(event -> listener.onMoveLayerUpButtonSelection());
    view.getMoveLayerDownMenuItem().setOnAction(event -> listener.onMoveLayerDownButtonSelection());
  }

  public ContextMenu getContextMenu() {
    return view.getContextMenu();
  }
}

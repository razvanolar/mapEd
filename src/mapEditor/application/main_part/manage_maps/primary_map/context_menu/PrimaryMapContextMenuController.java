package mapEditor.application.main_part.manage_maps.primary_map.context_menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import mapEditor.application.main_part.manage_maps.utils.listeners.MapContextMenuListener;
import mapEditor.application.main_part.types.Controller;

/**
 *
 * Created by razvanolar on 21.03.2016.
 */
public class PrimaryMapContextMenuController implements Controller {

  public interface IPrimaryMapContextMenuView {
    MenuItem getSaveMenuItem();
    MenuItem getRenameMenuItem();
    MenuItem getDeleteMenuItem();
    MenuItem getCopyPathMenuItem();
    MenuItem getExportToHtmlMenuItem();
    MenuItem getChangeAttributesMenuItem();
    ContextMenu getContextMenu();
  }

  private IPrimaryMapContextMenuView view;
  private MapContextMenuListener listener;

  public PrimaryMapContextMenuController(IPrimaryMapContextMenuView view, MapContextMenuListener listener) {
    this.view = view;
    this.listener = listener;
  }

  @Override
  public void bind() {
    addListeners();
  }

  private void addListeners() {
    view.getSaveMenuItem().setOnAction(event -> listener.saveCurrentMap());

    view.getRenameMenuItem().setOnAction(event1 -> listener.renameCurrentMap());

    view.getDeleteMenuItem().setOnAction(event -> listener.deleteCurrentMap());
  }

  public ContextMenu getContextMenu() {
    return view.getContextMenu();
  }
}

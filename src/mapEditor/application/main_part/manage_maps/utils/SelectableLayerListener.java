package mapEditor.application.main_part.manage_maps.utils;

/**
 *
 * Created by razvanolar on 24.02.2016.
 */
public interface SelectableLayerListener {

  /**
   * Notify the listener if specified layer was selected.
   * @param selectedLayer
   * Selected SelectableLayerView
   * @param showContextMenu
   * TRUE if the context menu have to be shown (i.e. when selecting a layer using right mouse click).
   * @param x
   * Indicates the x coordinate for context menu.
   * @param y
   * Indicates the y coordinate for context menu.
   */
  void selectedLayerChanged(SelectableLayerView selectedLayer, boolean showContextMenu, double x, double y);

  void onEditLayerButtonSelection();
  void onDeleteLayerButtonSelection();
  void onMoveLayerUpButtonSelection();
  void onMoveLayerDownButtonSelection();
}

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
   */
  void selectedLayerChanged(SelectableLayerView selectedLayer);
}

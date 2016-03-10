package mapEditor.application.main_part.manage_maps.utils;

import mapEditor.application.main_part.app_utils.models.LayerModel;

/**
 * Implemented by ManageMapsController to 'listen' for changes in LayersController for the current map.
 *
 * Created by razvanolar on 29.02.2016.
 */
public interface MapLayersListener {

  void addLayer(LayerModel layer);
  void removeLayer(LayerModel layer);
  void moveLayerUp(LayerModel layer);
  void moveLayerDown(LayerModel layer);
  void selectedLayerChanged(LayerModel layer);
  void checkedLayerChanged(LayerModel layer);
}
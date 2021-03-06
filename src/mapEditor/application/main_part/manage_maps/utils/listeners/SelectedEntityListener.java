package mapEditor.application.main_part.manage_maps.utils.listeners;

import mapEditor.application.main_part.app_utils.models.AbstractDrawModel;

/**
 * Used by ManageTilesController to notify ManageMapsController when the selected tile was changed.
 * Created by razvanolar on 02.03.2016.
 */
public interface SelectedEntityListener {

  void selectedEntityChanged(AbstractDrawModel selectedTile);
}

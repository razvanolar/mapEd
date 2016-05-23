package mapEditor.application.main_part.manage_maps.utils.listeners;

import mapEditor.application.main_part.manage_maps.utils.SelectableBrushView;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileView;

/**
 *
 * Created by razvanolar on 01.03.2016.
 */
public interface SelectableTileListener {

  void selectedTileChanged(SelectableTileView selectedView);
  void selectedBrushChanged(SelectableBrushView brushView);
}

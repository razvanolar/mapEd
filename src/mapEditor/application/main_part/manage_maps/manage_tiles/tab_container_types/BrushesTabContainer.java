package mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types;

import mapEditor.application.main_part.app_utils.models.AbstractDrawModel;
import mapEditor.application.main_part.manage_maps.utils.SelectableBrushView;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileView;
import mapEditor.application.main_part.manage_maps.utils.TabType;

/**
 *
 * Created by razvanolar on 23.05.2016.
 */
public class BrushesTabContainer extends AbstractTabContainer {

  public BrushesTabContainer(String name, boolean detailed) {
    super(name, detailed, TabType.BRUSHES);
  }

  @Override
  public AbstractDrawModel getSelectedDrawModel() {
    SelectableTileView selectedTileView = getSelectedTileView();
    if (selectedTileView == null || !(selectedTileView instanceof SelectableBrushView))
      return null;
    return ((SelectableBrushView) selectedTileView).getBrushModel();
  }
}

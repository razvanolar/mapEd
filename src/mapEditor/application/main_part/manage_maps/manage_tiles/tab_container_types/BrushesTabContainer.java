package mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types;

import mapEditor.application.main_part.app_utils.models.AbstractDrawModel;
import mapEditor.application.main_part.app_utils.models.brush.BrushModel;
import mapEditor.application.main_part.manage_maps.utils.SelectableBrushView;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileView;
import mapEditor.application.main_part.manage_maps.utils.TabType;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 23.05.2016.
 */
public class BrushesTabContainer extends AbstractTabContainer {

  public BrushesTabContainer(String name, boolean detailed) {
    super(name, detailed, TabType.BRUSHES);
  }

  public List<BrushModel> getBrushModels() {
    List<BrushModel> brushes = new ArrayList<>();
    if (selectableTileViews == null || selectableTileViews.isEmpty())
      return brushes;
    selectableTileViews.stream().filter(selectableView -> selectableView instanceof SelectableBrushView).forEach(selectableView -> {
      SelectableBrushView brushView = (SelectableBrushView) selectableView;
      brushes.add(brushView.getBrushModel());
    });
    return brushes;
  }

  @Override
  public AbstractDrawModel getSelectedDrawModel() {
    SelectableTileView selectedTileView = getSelectedTileView();
    if (selectedTileView == null || !(selectedTileView instanceof SelectableBrushView))
      return null;
    return ((SelectableBrushView) selectedTileView).getBrushModel();
  }
}

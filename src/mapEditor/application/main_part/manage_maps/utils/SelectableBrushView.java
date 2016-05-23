package mapEditor.application.main_part.manage_maps.utils;

import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectableTileListener;
import mapEditor.application.main_part.app_utils.models.brush.BrushModel;

/**
 *
 * Created by razvanolar on 22.05.2016.
 */
public class SelectableBrushView extends SelectableTileView {

  private BrushModel brushModel;

  public SelectableBrushView(BrushModel brushModel, boolean isDetailed, SelectableTileListener listener) {
    super(brushModel.getPrimaryImageModel(), isDetailed, listener, brushModel.getName());
    this.brushModel = brushModel;
  }

  @Override
  public void select() {
    selected = true;
    if (detailedView)
      detailedContainer.setBackground(AppParameters.SELECTED_DETAILED_TILE_BG);
    listener.selectedBrushChanged(this);
  }

  public BrushModel getBrushModel() {
    return brushModel;
  }
}

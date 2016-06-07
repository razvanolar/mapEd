package mapEditor.application.main_part.manage_maps.utils;

import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.object.ObjectModel;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectableTileListener;

/**
 *
 * Created by razvanolar on 07.06.2016.
 */
public class SelectableObjectView extends SelectableTileView {

  private ObjectModel obeject;

  public SelectableObjectView(ObjectModel obeject, boolean isDetailed, SelectableTileListener listener) {
    super(obeject.getPrimaryTile().getImage(), isDetailed, listener, obeject.getName());
    this.obeject = obeject;
  }

  @Override
  public void select() {
    selected = true;
    if (detailedView)
      detailedContainer.setBackground(AppParameters.SELECTED_DETAILED_TILE_BG);
    listener.selectedObjectChanged(this);
  }

  public ObjectModel getObejectModel() {
    return obeject;
  }
}

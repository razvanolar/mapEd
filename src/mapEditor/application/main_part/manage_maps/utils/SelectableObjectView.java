package mapEditor.application.main_part.manage_maps.utils;

import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.object.ObjectModel;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectableEntityListener;

/**
 *
 * Created by razvanolar on 07.06.2016.
 */
public class SelectableObjectView extends SelectableTileView {

  private ObjectModel object;

  public SelectableObjectView(ObjectModel object, boolean isDetailed, SelectableEntityListener listener) {
    super(object.getPrimaryTile().getImage(), isDetailed, listener, object.getName());
    this.object = object;
  }

  @Override
  public void select() {
    selected = true;
    if (detailedView)
      detailedContainer.setBackground(AppParameters.SELECTED_DETAILED_TILE_BG);
    listener.selectedObjectChanged(this);
  }

  public ObjectModel getObjectModel() {
    return object;
  }
}

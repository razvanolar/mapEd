package mapEditor.application.main_part.manage_images.manage_tile_sets.utils.listeners;

import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views.SelectableCreateEntityView;

/**
 *
 * Created by razvanolar on 05.06.2016.
 */
public interface CreateEntityListener {

  void removeEntityField(SelectableCreateEntityView selectableCreateBrushView);
  void entityNameChanged(SelectableCreateEntityView selectableCreateBrushView);
}

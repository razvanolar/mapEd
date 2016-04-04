package mapEditor.application.main_part.manage_images.manage_tile_sets.utils;

import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views.SelectableCreateBrushView;

/**
 *
 * Created by razvanolar on 04.04.2016.
 */
public interface CreateBrushListener {

  void removeBrushField(SelectableCreateBrushView selectableCreateBrushView);
  void brushNameChanged(SelectableCreateBrushView selectableCreateBrushView);
}

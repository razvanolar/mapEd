package mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types;

import mapEditor.application.main_part.app_utils.models.AbstractDrawModel;
import mapEditor.application.main_part.app_utils.models.object.ObjectModel;
import mapEditor.application.main_part.manage_maps.utils.SelectableObjectView;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileView;
import mapEditor.application.main_part.manage_maps.utils.TabType;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 07.06.2016.
 */
public class ObjectsTabContainer extends AbstractTabContainer {

  public ObjectsTabContainer(String name, boolean detailed) {
    super(name, detailed, TabType.OBJECTS);
  }

  public List<ObjectModel> getObjectModels() {
    List<ObjectModel> objectModels = new ArrayList<>();
    if (selectableTileViews == null || selectableTileViews.isEmpty())
      return objectModels;
    selectableTileViews.stream().filter(selectableTileView -> selectableTileView != null && selectableTileView instanceof SelectableObjectView).forEach(selectableTileView -> {
      SelectableObjectView selectableObjectView = (SelectableObjectView) selectableTileView;
      objectModels.add(selectableObjectView.getObjectModel());
    });
    return objectModels;
  }

  @Override
  public AbstractDrawModel getSelectedDrawModel() {
    SelectableTileView selectedTileView = getSelectedTileView();
    if (selectedTileView == null || !(selectedTileView instanceof SelectableObjectView))
      return null;
    return ((SelectableObjectView) selectedTileView).getObjectModel();
  }
}

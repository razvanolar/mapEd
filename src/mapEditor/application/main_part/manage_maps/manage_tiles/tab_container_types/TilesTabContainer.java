package mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types;

import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileView;
import mapEditor.application.main_part.manage_maps.utils.TabType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by razvanolar on 01.03.2016.
 */
public class TilesTabContainer extends AbstractTabContainer {

  public TilesTabContainer(String name, boolean detailed) {
    super(name, detailed, TabType.TILES);
  }

  public List<ImageModel> getTileModels() {
    List<ImageModel> tiles = new ArrayList<>();
    if (selectableTileViews == null || selectableTileViews.isEmpty())
      return tiles;
    tiles.addAll(selectableTileViews.stream().map(SelectableTileView::getImage).collect(Collectors.toList()));
    return tiles;
  }

  public ImageModel getSelectedTile() {
    SelectableTileView tileView = getSelectedTileView();
    return tileView != null ? tileView.getImage() : null;
  }
}

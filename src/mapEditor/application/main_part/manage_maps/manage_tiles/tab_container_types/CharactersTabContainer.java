package mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types;

import mapEditor.application.main_part.app_utils.models.AbstractDrawModel;
import mapEditor.application.main_part.app_utils.models.character.CharacterModel;
import mapEditor.application.main_part.manage_maps.utils.SelectableCharacterView;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileView;
import mapEditor.application.main_part.manage_maps.utils.TabType;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 20.06.2016.
 */
public class CharactersTabContainer extends AbstractTabContainer {

  public CharactersTabContainer(String name, boolean detailed) {
    super(name, detailed, TabType.CHARACTERS);
  }

  public List<CharacterModel> getCharacterModels() {
    List<CharacterModel> characterModels = new ArrayList<>();
    if (selectableTileViews == null || selectableTileViews.isEmpty())
      return characterModels;
    selectableTileViews.stream().filter(selectableTileView -> selectableTileView != null && selectableTileView instanceof SelectableCharacterView).forEach(selectableTileView -> {
      SelectableCharacterView selectableCharacterView = (SelectableCharacterView) selectableTileView;
      characterModels.add(selectableCharacterView.getCharacterModel());
    });
    return characterModels;
  }

  @Override
  public AbstractDrawModel getSelectedDrawModel() {
    SelectableTileView selectedTileView = getSelectedTileView();
    if (selectedTileView == null || !(selectedTileView instanceof SelectableCharacterView))
      return null;
    return ((SelectableCharacterView) selectedTileView).getCharacterModel();
  }
}

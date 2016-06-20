package mapEditor.application.main_part.manage_maps.utils;

import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.character.CharacterModel;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectableEntityListener;

/**
 *
 * Created by razvanolar on 20.06.2016.
 */
public class SelectableCharacterView extends SelectableTileView {

  private CharacterModel character;

  public SelectableCharacterView(CharacterModel character, boolean detailed, SelectableEntityListener listener) {
    super(character.getPreviewImage(), detailed, listener, character.getName());
    this.character = character;
  }

  @Override
  public void select() {
    selected = true;
    if (detailedView)
      detailedContainer.setBackground(AppParameters.SELECTED_DETAILED_TILE_BG);
    listener.selectedCharacterChanged(this);
  }

  public CharacterModel getCharacterModel() {
    return character;
  }
}

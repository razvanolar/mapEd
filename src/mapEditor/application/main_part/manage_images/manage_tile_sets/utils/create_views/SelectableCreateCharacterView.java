package mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views;

import javafx.scene.image.ImageView;
import mapEditor.application.main_part.app_utils.models.character.CharacterModel;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.listeners.CreateEntityListener;

/**
 *
 * Created by razvanolar on 16.06.2016.
 */
public class SelectableCreateCharacterView extends SelectableCreateEntityView {

  private CharacterModel characterModel;

  public SelectableCreateCharacterView(CharacterModel characterModel, CreateEntityListener listener) {
    super(new ImageView(characterModel.getPreviewImage().getImage()), listener);
    this.characterModel = characterModel;
  }

  public CharacterModel getCharacterModel() {
    return characterModel;
  }

  @Override
  public void setModelName(String value) {
    characterModel.setName(value);
  }
}

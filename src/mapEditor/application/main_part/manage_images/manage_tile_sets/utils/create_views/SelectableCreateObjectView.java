package mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views;

import javafx.scene.image.ImageView;
import mapEditor.application.main_part.app_utils.models.object.ObjectModel;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.listeners.CreateEntityListener;

/**
 *
 * Created by razvanolar on 05.06.2016.
 */
public class SelectableCreateObjectView extends SelectableCreateEntityView {

  private ObjectModel objectModel;

  public SelectableCreateObjectView(ObjectModel objectModel, CreateEntityListener listener) {
    super(new ImageView(objectModel.getPrimaryTile().getImage().getImage()), listener);
    this.objectModel = objectModel;
  }

  public ObjectModel getObjectModel() {
    return objectModel;
  }

  @Override
  public void setModelName(String value) {
    objectModel.setName(value);
  }
}

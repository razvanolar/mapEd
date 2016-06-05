package mapEditor.application.main_part.manage_images.manage_tile_sets.create_object;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.CreateEntityView;

/**
 *
 * Created by razvanolar on 05.06.2016.
 */
public class CreateObjectView extends CreateEntityView implements CreateObjectController.ICreateObjectView {

  public CreateObjectView() {
    super("Create New Object");
  }

  public void addCanvasContainer(Region region) {
    super.addCanvasContainer(region);
  }

  @Override
  public Button getAddObjectButton() {
    return addButton;
  }

  @Override
  public void addObjectView(Region region) {
    addEntityView(region);
  }

  @Override
  public void removeObjectView(Region region) {
    removeEntityView(region);
  }

  @Override
  public Region getNode() {
    return mainContainer;
  }
}

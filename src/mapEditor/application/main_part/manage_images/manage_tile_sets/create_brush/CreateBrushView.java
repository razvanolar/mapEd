package mapEditor.application.main_part.manage_images.manage_tile_sets.create_brush;

import javafx.scene.control.*;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.CreateEntityView;

/**
 *
 * Created by razvanolar on 03.04.2016.
 */
public class CreateBrushView extends CreateEntityView implements CreateBrushController.ICreateBrushView {

  public CreateBrushView() {
    super("Add New Brush");
  }

  public void addCanvasContainer(Region region) {
    super.addCanvasContainer(region);
  }

  public Button getAddBrushButton() {
    return addButton;
  }

  public TextField getPathTextField() {
    return pathTextField;
  }

  public Button getPathButton() {
    return pathButton;
  }

  public void addBrushView(Region region) {
    fieldsContainer.getChildren().add(region);
  }

  public void removeBrushView(Region region) {
    fieldsContainer.getChildren().remove(region);
  }

  @Override
  public Region getNode() {
    return super.asNode();
  }
}

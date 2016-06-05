package mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views;

import javafx.scene.image.ImageView;
import mapEditor.application.main_part.app_utils.models.brush.LWBrushModel;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.listeners.CreateEntityListener;

/**
 *
 * Created by razvanolar on 04.04.2016.
 */
public class SelectableCreateBrushView extends SelectableCreateEntityView {

  private LWBrushModel brushModel;

  public SelectableCreateBrushView(LWBrushModel brushModel, CreateEntityListener listener) {
    super(new ImageView(brushModel.getPrimaryImage().getImage()), listener);
    this.brushModel = brushModel;
  }

  @Override
  public void setModelName(String value) {
    brushModel.setName(value);
  }

  public LWBrushModel getBrushModel() {
    return brushModel;
  }
}

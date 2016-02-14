package mapEditor.application.main_part.manage_images.cropped_tiles;

import javafx.scene.control.Button;
import mapEditor.application.main_part.manage_images.utils.ManageImagesListener;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public class CroppedTileController implements Controller {

  public interface ICroppedTileView extends View {
    Button getSaveButton();
    Button getDropButton();
  }

  private ICroppedTileView view;
  private ManageImagesListener listener;

  public CroppedTileController(ICroppedTileView view, ManageImagesListener listener) {
    this.view = view;
    this.listener = listener;
  }

  @Override
  public void bind() {
    addListeners();
  }

  private void addListeners() {
    view.getSaveButton().setOnAction(event1 -> listener.saveCroppedImage(view));

    view.getDropButton().setOnAction(event -> listener.dropCroppedTileView(view));
  }
}

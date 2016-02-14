package mapEditor.application.main_part.manage_images.cropped_tiles;

import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public class CroppedTileController implements Controller {

  public interface ICroppedTileView extends View {

  }

  private ICroppedTileView view;

  public CroppedTileController(ICroppedTileView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    addListeners();
  }

  private void addListeners() {

  }
}

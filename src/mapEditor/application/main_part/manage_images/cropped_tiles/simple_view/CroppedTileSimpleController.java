package mapEditor.application.main_part.manage_images.cropped_tiles.simple_view;

import javafx.scene.image.Image;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.util.List;

/**
 *
 * Created by razvanolar on 15.02.2016.
 */
public class CroppedTileSimpleController implements Controller {

  public interface ICroppedTileSimpleView extends View {
    void addImage(ImageModel image);
    List<ImageModel> getImages();
  }

  private ICroppedTileSimpleView view;

  public CroppedTileSimpleController(ICroppedTileSimpleView view) {
    this.view = view;
  }

  @Override
  public void bind() {

  }

  public void addImage(ImageModel image) {
    view.addImage(image);
  }

  public void addImages(List<ImageModel> images) {
    images.forEach(view::addImage);
  }

  public List<ImageModel> getImages() {
    return view.getImages();
  }
}

package mapEditor.application.main_part.manage_images.cropped_tiles.simple_view;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.manage_images.cropped_tiles.simple_view.context_menu.SimpleCroppedTileContextMenuController;
import mapEditor.application.main_part.manage_images.cropped_tiles.simple_view.context_menu.SimpleCroppedTileContextMenuView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 15.02.2016.
 */
public class CroppedTileSimpleController implements Controller {

  public interface ICroppedTileSimpleView extends View {
    void addImage(ImageView image);
  }

  private ICroppedTileSimpleView view;
  private SimpleCroppedTileContextMenuController contextMenuController;
  private List<ImageModel> images;

  public CroppedTileSimpleController(ICroppedTileSimpleView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    images = new ArrayList<>();
  }

  public void addImage(ImageModel image) {
    ImageView imageView = new ImageView(image.getImage());
    view.addImage(imageView);
    images.add(image);
    addImageViewListeners(imageView);
  }

  public void addImages(List<ImageModel> images) {
    for (ImageModel image : images) {
      ImageView imageView = new ImageView(image.getImage());
      view.addImage(imageView);
      this.images.add(image);
      addImageViewListeners(imageView);
    }
  }

  private void addImageViewListeners(ImageView imageView) {
    imageView.setOnMouseClicked(event -> {
      if (event.getButton() == MouseButton.PRIMARY || !(event.getSource() instanceof ImageView))
        return;
      ImageView image = (ImageView) event.getSource();
      getContextMenuController().showContextMenu(image);
    });
  }

  public List<ImageModel> getImages() {
    return images;
  }

  public SimpleCroppedTileContextMenuController getContextMenuController() {
    if (contextMenuController == null) {
      contextMenuController = new SimpleCroppedTileContextMenuController(new SimpleCroppedTileContextMenuView());
      contextMenuController.bind();
    }
    return contextMenuController;
  }
}

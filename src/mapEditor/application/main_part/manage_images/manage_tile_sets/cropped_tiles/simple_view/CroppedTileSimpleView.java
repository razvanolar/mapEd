package mapEditor.application.main_part.manage_images.manage_tile_sets.cropped_tiles.simple_view;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

/**
 *
 * Created by razvanolar on 15.02.2016.
 */
public class CroppedTileSimpleView implements CroppedTileSimpleController.ICroppedTileSimpleView {

  private FlowPane mainContainer;

  public CroppedTileSimpleView() {
    initGUI();
  }

  private void initGUI() {
    mainContainer = new FlowPane(Orientation.HORIZONTAL, 5, 5);
    mainContainer.setAlignment(Pos.CENTER);
  }

  public void addImage(ImageView image) {
    mainContainer.getChildren().add(image);
  }

  public void clearImages() {
    mainContainer.getChildren().clear();
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

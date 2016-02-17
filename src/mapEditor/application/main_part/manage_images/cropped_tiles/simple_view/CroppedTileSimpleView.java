package mapEditor.application.main_part.manage_images.cropped_tiles.simple_view;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.app_utils.models.ImageModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 15.02.2016.
 */
public class CroppedTileSimpleView implements CroppedTileSimpleController.ICroppedTileSimpleView {

  private FlowPane mainContainer;
  private List<ImageModel> images;

  public CroppedTileSimpleView() {
    initGUI();
    images = new ArrayList<>();
  }

  private void initGUI() {
    mainContainer = new FlowPane(Orientation.HORIZONTAL, 5, 5);
    mainContainer.setAlignment(Pos.CENTER);
  }

  public void addImage(ImageView image) {
    mainContainer.getChildren().add(image);
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

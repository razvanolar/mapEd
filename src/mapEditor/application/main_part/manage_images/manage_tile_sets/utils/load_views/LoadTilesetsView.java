package mapEditor.application.main_part.manage_images.manage_tile_sets.utils.load_views;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * Created by razvanolar on 22.03.2016.
 */
public class LoadTileSetsView implements LoadTileSetsController.ILoadTileSetsView {

  private ScrollPane mainContainer;
  private VBox fieldsContainer;

  public LoadTileSetsView() {
    initGUI();
  }

  private void initGUI() {
    fieldsContainer = new VBox(5);
    mainContainer = new ScrollPane(fieldsContainer);

    fieldsContainer.setPadding(new Insets(5));

    mainContainer.setFitToWidth(true);
    mainContainer.setMinWidth(450);
    mainContainer.setMinHeight(200);
  }

  @Override
  public void addField(Region node) {
    fieldsContainer.getChildren().add(node);
  }

  @Override
  public void removeField(Region node) {
    fieldsContainer.getChildren().remove(node);
  }

  public void setPath(String path) {
    fieldsContainer.getChildren().add(0, new Text(path));
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}
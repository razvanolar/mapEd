package mapEditor.application.main_part.manage_images.cropped_tiles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.constants.CssConstants;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public class CroppedTileView implements CroppedTileController.ICroppedTileView {

  private GridPane mainContainer;
  private TextField nameTextField;
  private TextField pathTextField;
  private Button pathButton;
  private Button saveButton;
  private Button dropButton;

  private Image image;

  public CroppedTileView(Image image) {
    this.image = image;
    initGUI();
  }

  private void initGUI() {
    nameTextField = new TextField();
    pathTextField = new TextField();
    pathButton = new Button("...");
    saveButton = new Button("Save");
    dropButton = new Button("Drop");
    mainContainer = new GridPane();
    GridPane imageContainer = new GridPane();

    pathTextField.setMinWidth(200);

    imageContainer.setAlignment(Pos.CENTER);
    imageContainer.add(new ImageView(image), 0, 0);

    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setHgap(5);
    mainContainer.setVgap(5);
    mainContainer.add(imageContainer, 0, 0, 1, 2);
    mainContainer.add(new Text("Tile Name: "), 1, 0);
    mainContainer.add(nameTextField, 2, 0, 2, 1);
    mainContainer.add(saveButton, 4, 0);
    mainContainer.add(new Text("Tile Path: "), 1, 1);
    mainContainer.add(pathTextField, 2, 1);
    mainContainer.add(pathButton, 3, 1);
    mainContainer.add(dropButton, 4, 1);
    mainContainer.getStyleClass().add(CssConstants.CROPPED_TILE_VIEW);
    mainContainer.setPadding(new Insets(3));
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

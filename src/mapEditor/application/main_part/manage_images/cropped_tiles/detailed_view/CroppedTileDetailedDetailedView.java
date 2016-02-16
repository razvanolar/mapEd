package mapEditor.application.main_part.manage_images.cropped_tiles.detailed_view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public class CroppedTileDetailedDetailedView implements CroppedTilesDetailedController.ICroppedTileDetailedView {

  private GridPane mainContainer;
  private TextField nameTextField;
  private TextField pathTextField;
  private Button pathButton;
  private Button saveButton;
  private Button dropButton;

  private Image image;

  public CroppedTileDetailedDetailedView(Image image) {
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

    nameTextField.setPrefWidth(120);
    pathTextField.setMinWidth(200);

    imageContainer.setAlignment(Pos.CENTER);
    imageContainer.add(new ImageView(image), 0, 0);

    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setHgap(5);
    mainContainer.add(imageContainer, 0, 0);
    mainContainer.add(new Text("Name: "), 1, 0);
    mainContainer.add(nameTextField, 2, 0);
    mainContainer.add(new Text("Path: "), 3, 0);
    mainContainer.add(pathTextField, 4, 0);
    mainContainer.add(pathButton, 5, 0);
    mainContainer.add(saveButton, 6, 0);
    mainContainer.add(dropButton, 7, 0);
//    mainContainer.getStyleClass().add(CssConstants.CROPPED_TILE_VIEW);
//    mainContainer.setPadding(new Insets(3));
  }

  public TextField getNameTextField() {
    return nameTextField;
  }

  public TextField getPathTextField() {
    return pathTextField;
  }

  public Button getPathButton() {
    return pathButton;
  }

  public Button getSaveButton() {
    return saveButton;
  }

  public Button getDropButton() {
    return dropButton;
  }

  public Image getImage() {
    return image;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

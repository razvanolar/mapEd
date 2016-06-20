package mapEditor.application.main_part.manage_images.manage_tile_sets.create_character;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.CreateEntityView;

/**
 *
 * Created by razvanolar on 10.06.2016.
 */
public class CreateCharacterView extends CreateEntityView implements CreateCharacterController.ICreateCharacterView {

  private Spinner<Integer> widthSpinner;
  private Spinner<Integer> heightSpinner;
  private Button firstButton;
  private Button secondButton;
  private Button thirdButton;
  private Button forthButton;
  private int maxWidth;
  private int maxHeight;

  public CreateCharacterView(int maxWidth, int maxHeight) {
    super("Create New Characters");
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
    adjustGUI();
  }

  private void adjustGUI() {
    int cellSize = AppParameters.CURRENT_PROJECT.getCellSize();
    widthSpinner = new Spinner<>(cellSize, maxWidth, cellSize, 1);
    heightSpinner = new Spinner<>(cellSize, maxHeight, cellSize, 1);
    firstButton = new Button();
    secondButton = new Button();
    thirdButton = new Button();
    forthButton = new Button();

    HBox orderButtonsContainer = new HBox(5, firstButton, new Text("-"), secondButton, new Text("-"),
            thirdButton, new Text("-"), forthButton);
    orderButtonsContainer.setAlignment(Pos.CENTER);
    int buttonsWidth = 50;
    firstButton.setPrefWidth(buttonsWidth);
    secondButton.setPrefWidth(buttonsWidth);
    thirdButton.setPrefWidth(buttonsWidth);
    forthButton.setPrefWidth(buttonsWidth);

    GridPane selectionSizeContainer = new GridPane();
    selectionSizeContainer.setAlignment(Pos.CENTER);
    selectionSizeContainer.setHgap(5);
    selectionSizeContainer.setVgap(5);
    selectionSizeContainer.add(new Text("Width Value : "), 0, 0);
    selectionSizeContainer.add(widthSpinner, 1, 0);
    selectionSizeContainer.add(new Text("Height Value : "), 0, 1);
    selectionSizeContainer.add(heightSpinner, 1, 1);

    HBox selectionTitleContainer = new HBox(new Text("Selection Size"));
    HBox orderTitleContainer = new HBox(new Text("Directions Order"));
    selectionTitleContainer.setAlignment(Pos.CENTER);
    orderTitleContainer.setAlignment(Pos.CENTER);
    selectionTitleContainer.getStyleClass().add(CssConstants.TITLE_LABEL_BG);
    orderTitleContainer.getStyleClass().add(CssConstants.TITLE_LABEL_BG);

    VBox sizeContainer = new VBox(3);
    sizeContainer.setAlignment(Pos.TOP_CENTER);
    sizeContainer.setPadding(new Insets(5));
    sizeContainer.getChildren().addAll(orderTitleContainer, orderButtonsContainer, selectionTitleContainer, selectionSizeContainer);

    leftPane.setBottom(sizeContainer);
  }

  public Spinner<Integer> getWidthSpinner() {
    return widthSpinner;
  }

  public Spinner<Integer> getHeightSpinner() {
    return heightSpinner;
  }

  public Button getFirstButton() {
    return firstButton;
  }

  public Button getSecondButton() {
    return secondButton;
  }

  public Button getThirdButton() {
    return thirdButton;
  }

  public Button getForthButton() {
    return forthButton;
  }

  public void addCanvasContainer(Region region) {
    super.addCanvasContainer(region);
  }

  @Override
  public Button getAddCharacterButton() {
    return addButton;
  }

  @Override
  public void addCharacterView(Region region) {
    addEntityView(region);
  }

  @Override
  public void removeCharacterView(Region region) {
    removeEntityView(region);
  }

  @Override
  public Region getNode() {
    return mainContainer;
  }
}

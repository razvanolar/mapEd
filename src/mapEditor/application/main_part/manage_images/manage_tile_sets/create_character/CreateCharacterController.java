package mapEditor.application.main_part.manage_images.manage_tile_sets.create_character;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageMatrix;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.character.CharacterModel;
import mapEditor.application.main_part.app_utils.views.canvas.CharacterCanvas;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views.SelectableCreateCharacterView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views.SelectableCreateEntityView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.listeners.CreateEntityListener;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by razvanolar on 10.06.2016.
 */
public class CreateCharacterController implements Controller, CreateEntityListener {

  private enum CharacterDirections {
    UP("Up"), DOWN("Down"), LEFT("Left"), RIGHT("Right");

    String text;

    CharacterDirections(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }
  }

  public interface ICreateCharacterView extends View {
    Button getAddCharacterButton();
    Button getPathButton();
    void addCharacterView(Region region);
    void removeCharacterView(Region region);
    void addCanvasContainer(Region region);
    Spinner<Integer> getWidthSpinner();
    Spinner<Integer> getHeightSpinner();
    Button getFirstButton();
    Button getSecondButton();
    Button getThirdButton();
    Button getForthButton();
    Region getNode();
    TextField getPathTextField();
  }

  private ICreateCharacterView view;
  private Image image;
  private Button completeSelectionNode;
  private Window parent;
  private int selectionWidth;
  private int selectionHeight;
  private Color strokeColor;
  private Color fillColor;

  private List<SelectableCreateCharacterView> characterViews;
  private List<CharacterDirections> directions;
  private CharacterCanvas characterCanvas;

  public CreateCharacterController(ICreateCharacterView view, Image image, Button completeSelectionNode, Window parent,
                                   int selectionWidth, int selectionHeight, Color strokeColor, Color fillColor) {
    this.view = view;
    this.image = image;
    this.completeSelectionNode = completeSelectionNode;
    this.parent = parent;
    this.selectionWidth = selectionWidth;
    this.selectionHeight = selectionHeight;
    this.strokeColor = strokeColor;
    this.fillColor = fillColor;
  }

  @Override
  public void bind() {
    characterViews = new ArrayList<>();
    directions = new ArrayList<>();
    directions.add(CharacterDirections.UP);
    directions.add(CharacterDirections.DOWN);
    directions.add(CharacterDirections.LEFT);
    directions.add(CharacterDirections.RIGHT);

    characterCanvas = new CharacterCanvas(image);
    characterCanvas.setSquareBorderColor(strokeColor);
    characterCanvas.setSquareFillColor(fillColor);
    characterCanvas.setSelectionWidth(selectionWidth);
    characterCanvas.setSelectionHeight(selectionHeight);
    ScrollPane scrollPane = new ScrollPane(characterCanvas);

    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);

    ChangeListener<Number> listener = (observable, oldValue, newValue) -> characterCanvas.paint();
    characterCanvas.widthProperty().bind(scrollPane.widthProperty());
    characterCanvas.heightProperty().bind(scrollPane.heightProperty());
    scrollPane.widthProperty().addListener(listener);
    scrollPane.heightProperty().addListener(listener);

    view.addCanvasContainer(scrollPane);
    view.getPathTextField().setText(AppParameters.CURRENT_PROJECT.getObjectsFile().getAbsolutePath());

    completeSelectionNode.setDisable(true);
    view.getWidthSpinner().getValueFactory().setValue(selectionWidth);
    view.getHeightSpinner().getValueFactory().setValue(selectionHeight);
    view.getPathTextField().setText(AppParameters.CURRENT_PROJECT.getCharactersFile().getAbsolutePath());
    addListeners();
  }

  private void addListeners() {
    view.getWidthSpinner().valueProperty().addListener((observable, oldValue, newValue) -> {
      characterCanvas.setSelectionWidth(newValue);
      selectionWidth = newValue;
    });

    view.getHeightSpinner().valueProperty().addListener((observable, oldValue, newValue) -> {
      characterCanvas.setSelectionHeight(newValue);
      selectionHeight = newValue;
    });

    view.getAddCharacterButton().setOnAction(event -> onCreateCharacterSelection());

    // set default order
    view.getFirstButton().setText(CharacterDirections.DOWN.getText());
    view.getFirstButton().setUserData(CharacterDirections.DOWN);
    view.getSecondButton().setText(CharacterDirections.LEFT.getText());
    view.getSecondButton().setUserData(CharacterDirections.LEFT);
    view.getThirdButton().setText(CharacterDirections.RIGHT.getText());
    view.getThirdButton().setUserData(CharacterDirections.RIGHT);
    view.getForthButton().setText(CharacterDirections.UP.getText());
    view.getForthButton().setUserData(CharacterDirections.UP);

    EventHandler<ActionEvent> actionEventEventHandler = event -> {
      if (event.getSource() instanceof Button) {
        Button button = (Button) event.getSource();
        if (button.getUserData() != null && button.getUserData() instanceof CharacterDirections) {
          CharacterDirections direction = (CharacterDirections) button.getUserData();
          CharacterDirections nextDirection = getNextDirection(direction);
          button.setText(nextDirection.getText());
          button.setUserData(nextDirection);
          view.getAddCharacterButton().setDisable(!isValidDirectionsOrder());
        }
      }
    };
    view.getFirstButton().setOnAction(actionEventEventHandler);
    view.getSecondButton().setOnAction(actionEventEventHandler);
    view.getThirdButton().setOnAction(actionEventEventHandler);
    view.getForthButton().setOnAction(actionEventEventHandler);

    view.getPathButton().setOnAction(event -> {
      OkCancelDialog dialog = new OkCancelDialog("Select Characters Path", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true, true, parent);

      SystemFilesView filesView = new SystemFilesView(dialog.getOkButton(), AppParameters.CURRENT_PROJECT.getCharactersFile(), true, null);
      dialog.getOkButton().setOnAction(event1 -> {
        String selectedPath = filesView.getSelectedPath();
        if (StringValidator.isValidCharacterPath(selectedPath)) {
          view.getPathTextField().setText(selectedPath);
          completeSelectionNode.setDisable(!isValidSelection());
        }
        dialog.close();
      });

      dialog.setContent(filesView.asNode());
      dialog.show();
    });
  }

  private boolean isValidDirectionsOrder() {
    return !(checkWithOthers(view.getFirstButton()) ||
            checkWithOthers(view.getSecondButton()) ||
            checkWithOthers(view.getThirdButton()) ||
            checkWithOthers(view.getForthButton()));
  }

  private boolean checkWithOthers(Button source) {
    Button firstButton = view.getFirstButton();
    Button secondButton = view.getSecondButton();
    Button thirdButton = view.getThirdButton();
    Button forthButton = view.getForthButton();
    CharacterDirections firstDirection = (CharacterDirections) firstButton.getUserData();
    CharacterDirections secondDirection = (CharacterDirections) secondButton.getUserData();
    CharacterDirections thirdDirection = (CharacterDirections) thirdButton.getUserData();
    CharacterDirections forthDirection = (CharacterDirections) forthButton.getUserData();
    CharacterDirections sourceDirection = (CharacterDirections) source.getUserData();

    return (source != firstButton && sourceDirection == firstDirection) ||
            (source != secondButton && sourceDirection == secondDirection) ||
            (source != thirdButton && sourceDirection == thirdDirection) ||
            (source != forthButton && sourceDirection == forthDirection);
  }

  private CharacterDirections getNextDirection(CharacterDirections direction) {
    int index = directions.indexOf(direction);
    return index == 3 ? directions.get(0) : directions.get(index + 1);
  }

  private void onCreateCharacterSelection() {
    if (!isValidDirectionsOrder()) {
      Dialog.showWarningDialog(null, "Directions order is not correct.", parent);
      return;
    }
    boolean canVertical = characterCanvas.getSelectedCols() == 4;
    boolean canHorizontal = characterCanvas.getSelecteRows() == 4;
    if (!canHorizontal && !canVertical) {
      Dialog.showWarningDialog(null, "The selection is not correctly made.", parent);
      return;
    }

    CharacterModel characterModel = getSelectedModel();
    if (characterModel == null) {
      Dialog.showWarningDialog(null, "Character model can not be computed.", parent);
      return;
    }

    SelectableCreateCharacterView characterView = new SelectableCreateCharacterView(characterModel, this);
    view.addCharacterView(characterView.asNode());
    characterViews.add(characterView);
    completeSelectionNode.setDisable(!isValidSelection());
  }

  private CharacterModel getSelectedModel() {
    int cols = characterCanvas.getSelectedCols();
    ImageMatrix imageMatrix = characterCanvas.cropSelectedMatrix();
    int upIndex = getIndexForDirection(CharacterDirections.UP);
    int downIndex = getIndexForDirection(CharacterDirections.DOWN);
    int leftIndex = getIndexForDirection(CharacterDirections.LEFT);
    int rightIndex = getIndexForDirection(CharacterDirections.RIGHT);

    if (upIndex == -1 || downIndex == -1 || leftIndex == -1 || rightIndex == -1)
      return null;

    Image[][] matrix = imageMatrix.getMatrix();
    List<ImageModel> upTiles = new ArrayList<>(cols);
    List<ImageModel> downTiles = new ArrayList<>(cols);
    List<ImageModel> leftTiles = new ArrayList<>(cols);
    List<ImageModel> rightTiles = new ArrayList<>(cols);
    for (int i = 0; i < cols; i ++) {
      upTiles.add(new ImageModel(matrix[upIndex][i]));
      downTiles.add(new ImageModel(matrix[downIndex][i]));
      leftTiles.add(new ImageModel(matrix[leftIndex][i]));
      rightTiles.add(new ImageModel(matrix[rightIndex][i]));
    }

    return new CharacterModel(upTiles, downTiles, leftTiles, rightTiles);
  }

  private int getIndexForDirection(CharacterDirections direction) {
    Button firstButton = view.getFirstButton();
    CharacterDirections firstDirection = (CharacterDirections) firstButton.getUserData();
    if (direction == firstDirection)
      return 0;
    Button secondButton = view.getSecondButton();
    CharacterDirections secondDirection = (CharacterDirections) secondButton.getUserData();
    if (direction == secondDirection)
      return 1;
    Button thirdButton = view.getThirdButton();
    CharacterDirections thirdDirection = (CharacterDirections) thirdButton.getUserData();
    if (direction == thirdDirection)
      return 2;
    Button forthButton = view.getForthButton();
    CharacterDirections forthDirection = (CharacterDirections) forthButton.getUserData();
    if (direction == forthDirection)
      return 3;
    return -1;
  }

  public boolean isValidSelection() {
    return StringValidator.isValidCharacterPath(view.getPathTextField().getText()) && characterNamesAreValid();
  }

  private boolean characterNamesAreValid() {
    if (characterViews == null || characterViews.isEmpty())
      return false;
    for (SelectableCreateCharacterView characterView : characterViews) {
      CharacterModel characterModel = characterView.getCharacterModel();
      if (characterModel == null || !StringValidator.isValidFileName(characterModel.getName()))
        return false;
    }
    return true;
  }

  public List<CharacterModel> getCharacterModels() {
    if (characterViews == null || characterViews.isEmpty())
      return null;
    return characterViews.stream().map(SelectableCreateCharacterView::getCharacterModel).collect(Collectors.toList());
  }

  public String getSelectedPath() {
    return view.getPathTextField().getText();
  }

  @Override
  public void removeEntityField(SelectableCreateEntityView selectableCreateCharacterView) {
    if (selectableCreateCharacterView != null && selectableCreateCharacterView instanceof SelectableCreateCharacterView) {
      characterViews.remove(selectableCreateCharacterView);
      view.removeCharacterView(selectableCreateCharacterView.asNode());
    }
    completeSelectionNode.setDisable(!isValidSelection());
  }

  @Override
  public void entityNameChanged(SelectableCreateEntityView selectableCreateCharacterView) {
    completeSelectionNode.setDisable(!isValidSelection());
  }
}

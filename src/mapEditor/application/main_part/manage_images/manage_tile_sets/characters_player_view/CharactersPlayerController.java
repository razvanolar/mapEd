package mapEditor.application.main_part.manage_images.manage_tile_sets.characters_player_view;

import javafx.animation.Animation;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.models.ImageMatrix;
import mapEditor.application.main_part.app_utils.views.canvas.CharacterCanvas;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.CharacterPlayerListener;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 28.03.2016.
 */
public class CharactersPlayerController implements Controller, CharacterPlayerListener {

  private ImageView imageView;
  private CharacterCanvas characterCanvas;

  public interface ICharacterPlayerView extends View {
    void setContent(ScrollPane scrollPane, ImageView imageView);
    boolean isHorizontal();
    ToggleButton getHorizontalButton();
    Button getPlayButton();
  }

  private ICharacterPlayerView view;
  private ImageMatrix imageMatrix;
  private Color strokeColor;
  private Color fillColor;
  private int columns;
  private int rows;
  private int cellWidth;
  private int cellHeight;

  private SpriteAnimation animation;
  private boolean isPlaying;

  public CharactersPlayerController(ICharacterPlayerView view, ImageMatrix imageMatrix, Color strokeColor,
                                    Color fillColor, int columns, int rows, int cellWidth, int cellHeight) {
    this.view = view;
    this.imageMatrix = imageMatrix;
    this.strokeColor = strokeColor;
    this.fillColor = fillColor;
    this.columns = columns;
    this.rows = rows;
    this.cellWidth = cellWidth;
    this.cellHeight = cellHeight;
  }

  @Override
  public void bind() {
    characterCanvas = new CharacterCanvas(imageMatrix.getImage(), view.isHorizontal(), strokeColor, fillColor, cellWidth, cellHeight, this);
    ScrollPane scrollPane = new ScrollPane(characterCanvas);

    characterCanvas.widthProperty().bind(scrollPane.widthProperty());
    characterCanvas.heightProperty().bind(scrollPane.heightProperty());

    ChangeListener<Number> listener = (observable, oldValue, newValue) -> characterCanvas.paint();
    scrollPane.widthProperty().addListener(listener);
    scrollPane.heightProperty().addListener(listener);
    scrollPane.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);

    imageView = new ImageView(imageMatrix.getImage());
    view.setContent(scrollPane, imageView);

    addListeners();
  }

  private void addListeners() {
    view.getPlayButton().setOnAction(event -> {
      if (animation == null) {
        animation = new SpriteAnimation(imageView, new Duration(1000), columns, rows, cellWidth, cellHeight, view.isHorizontal(), characterCanvas.getStartIndex());
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setAutoReverse(true);
        animation.setRate(2.5);
      }
      if (!isPlaying) {
        animation.play();
        view.getPlayButton().setText("Stop");
        isPlaying = true;
      } else {
        animation.stop();
        view.getPlayButton().setText("Play");
        isPlaying = false;
      }
    });

    view.getHorizontalButton().selectedProperty().addListener((observable, oldValue, newValue) -> {
      changeSelectionDirection(newValue);
    });
  }

  private void changeSelectionDirection(boolean isHorizontal) {
    characterCanvas.setIsHorizontal(isHorizontal);
    characterCanvas.paint();
    if (animation != null)
      animation.setIsHorizontal(isHorizontal);
  }

  @Override
  public void selectionChanged() {
    if (animation != null)
      animation.setStartIndex(characterCanvas.getStartIndex());
  }

  public void closeAnimation() {
    if (animation != null)
      animation.stop();
  }
}

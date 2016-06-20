package mapEditor.application.main_part.manage_images.manage_tile_sets.characters_player_view;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

/**
 *
 * Created by razvanolar on 28.03.2016.
 */
public class CharactersPlayerView implements CharacterPlayerController.ICharacterPlayerView {

  private BorderPane mainContainer;
  private SplitPane splitPane;
  private ToggleButton horizontalButton;
  private Button playButton;

  public CharactersPlayerView() {
    initGUI();
  }

  private void initGUI() {
    ToggleButton verticalButton = new ToggleButton("Vertical");
    horizontalButton = new ToggleButton("Horizontal");
    playButton = new Button("Play");
    ToolBar toolBar = new ToolBar(verticalButton, horizontalButton, new Separator(Orientation.HORIZONTAL), playButton);
    splitPane = new SplitPane();
    mainContainer = new BorderPane(splitPane);

    ToggleGroup group = new ToggleGroup();
    group.getToggles().addAll(verticalButton, horizontalButton);
    horizontalButton.setSelected(true);

    mainContainer.setMinWidth(350);
    mainContainer.setMinHeight(200);
    mainContainer.setTop(toolBar);
  }

  @Override
  public void setContent(ScrollPane scrollPane, ImageView imageView) {
    splitPane.getItems().clear();
    splitPane.getItems().addAll(scrollPane, new BorderPane(imageView));
    splitPane.setDividerPositions(.65);
  }

  public ToggleButton getHorizontalButton() {
    return horizontalButton;
  }

  public Button getPlayButton() {
    return playButton;
  }

  public boolean isHorizontal() {
    return horizontalButton.isSelected();
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

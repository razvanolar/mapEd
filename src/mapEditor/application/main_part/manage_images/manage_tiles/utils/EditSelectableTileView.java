package mapEditor.application.main_part.manage_images.manage_tiles.utils;

import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.SystemParameters;

/**
 *
 * Created by razvanolar on 12.03.2016.
 */
public class EditSelectableTileView implements View {

  private static Pane hoverPane;
  private static EventHandler<MouseEvent> mousePressedHandler;

  private int size;
  private StackPane mainContainer;
  private ImageView contentTile;

  public EditSelectableTileView(ImageView contentTile, int size) {
    this.contentTile = contentTile;
    this.size = size;
    initGUI();
  }

  private void initGUI() {
    mainContainer = new StackPane(contentTile);

    mainContainer.setPrefWidth(size);
    mainContainer.setPrefHeight(size);
    mainContainer.setOnMousePressed(getMousePressedHandler());
    mainContainer.setUserData(this);
  }

  private void setSelected(boolean value) {
    if (value) {
      if (!mainContainer.getChildren().contains(getHoverPane()))
        mainContainer.getChildren().add(getHoverPane());
    }
  }

  public void setStyle(ColorAdjust colorAdjust) {
    contentTile.setEffect(colorAdjust);
  }

  public static Pane getHoverPane() {
    if (hoverPane == null) {
      hoverPane = new Pane();
      hoverPane.setBackground(new Background(new BackgroundFill(SystemParameters.EDIT_SELECTABLE_TILE_HOVER_COLOR, null, null)));
    }
    return hoverPane;
  }

  public EventHandler<MouseEvent> getMousePressedHandler() {
    if (mousePressedHandler == null) {
      mousePressedHandler = event -> {
        if (!(event.getSource() instanceof StackPane))
          return;
        StackPane stackPane = (StackPane) event.getSource();
        if (stackPane.getUserData() == null || !(stackPane.getUserData() instanceof EditSelectableTileView))
          return;

        EditSelectableTileView source = (EditSelectableTileView) stackPane.getUserData();
        source.setSelected(true);

        event.consume();
      };
    }
    return mousePressedHandler;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

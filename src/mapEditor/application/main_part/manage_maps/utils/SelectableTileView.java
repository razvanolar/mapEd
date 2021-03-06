package mapEditor.application.main_part.manage_maps.utils;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectableEntityListener;

/**
 *
 * Created by razvanolar on 01.03.2016.
 */
public class SelectableTileView extends StackPane {

  private static EventHandler<MouseEvent> onMouseEnteredListener;
  private static EventHandler<MouseEvent> onMouseExitedListener;
  private static EventHandler<MouseEvent> onMousePressedListener;

  protected HBox detailedContainer;
  protected ImageView imageView;

  protected ImageModel image;
  protected String name;
  protected boolean detailedView;
  protected boolean selected;
  protected SelectableEntityListener listener;

  public SelectableTileView(ImageModel image, boolean detailedView, SelectableEntityListener listener) {
    this.image = image;
    this.name = image.getName();
    this.detailedView = detailedView;
    this.imageView = new ImageView(image.getImage());
    this.listener = listener;
    setContentBasedOnViewType();

    this.setOnMouseEntered(getOnMouseEnteredListener());
    this.setOnMouseExited(getOnMouseExitedListener());
    this.setOnMousePressed(getOnMousePressedListener());
  }

  public SelectableTileView(ImageModel image, boolean detailedView, SelectableEntityListener listener, String name) {
    this(image, detailedView, listener);
    this.name = name;
    setContentBasedOnViewType();
  }

  private void setContentBasedOnViewType() {
    if (detailedView)
      changeToDetailedView();
    else
      changeToSimpleView();
  }

  private void changeToDetailedView() {
    if (detailedContainer == null) {
      detailedContainer = new HBox(9);
      detailedContainer.setAlignment(Pos.CENTER_LEFT);
      detailedContainer.setPadding(new Insets(2));
    } else
      detailedContainer.getChildren().clear();

    detailedContainer.getChildren().addAll(imageView, new Text(name));

    getChildren().clear();
    getChildren().add(detailedContainer);
  }

  private void changeToSimpleView() {

  }

  public void select() {
    selected = true;
    if (detailedView)
      detailedContainer.setBackground(AppParameters.SELECTED_DETAILED_TILE_BG);
    listener.selectedTileChanged(this);
  }

  public void unselect() {
    selected = false;
    if (detailedView)
      detailedContainer.setBackground(AppParameters.TRANSPARENT_BG);
  }

  private void onMouseEntered() {
    if (!selected && detailedView)
      detailedContainer.setBackground(AppParameters.HOVERED_DETAILED_TILE_BG);
  }

  private void onMouseExited() {
    if (!selected && detailedView)
      detailedContainer.setBackground(AppParameters.TRANSPARENT_BG);
  }

  private void onMouseClicked() {
    select();
  }

  private static EventHandler<MouseEvent> getOnMouseEnteredListener() {
    if (onMouseEnteredListener == null) {
      onMouseEnteredListener = event -> {
        SelectableTileView source = (SelectableTileView) event.getSource();
        source.onMouseEntered();
      };
    }
    return onMouseEnteredListener;
  }

  private static EventHandler<MouseEvent> getOnMouseExitedListener() {
    if (onMouseExitedListener == null) {
      onMouseExitedListener = event -> {
        SelectableTileView source = (SelectableTileView) event.getSource();
        source.onMouseExited();
      };
    }
    return onMouseExitedListener;
  }

  private static EventHandler<MouseEvent> getOnMousePressedListener() {
    if (onMousePressedListener == null) {
      onMousePressedListener = event -> {
        SelectableTileView source = (SelectableTileView) event.getSource();
        source.onMouseClicked();
      };
    }
    return onMousePressedListener;
  }

  public ImageModel getImage() {
    return image;
  }

  public boolean isSelected() {
    return selected;
  }
}

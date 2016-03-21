package mapEditor.application.main_part.manage_maps.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.LayerModel;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectableLayerListener;

/**
 *
 * Created by razvanolar on 23.02.2016.
 */
public class SelectableLayerView extends StackPane {

  private static EventHandler<MouseEvent> onMouseEnteredListener;
  private static EventHandler<MouseEvent> onMouseExitedListener;
  private static ChangeListener<Boolean> checkBoxSelectionListener;

  private static EventHandler<MouseEvent> onMouseClickedListener;
  private CheckBox checkBox;
  private Text text;

  private HBox container;
  private SelectableLayerListener listener;
  private LayerModel layerModel;
  private boolean isSelected;

  public SelectableLayerView(LayerModel layerModel, SelectableLayerListener listener) {
    this.layerModel = layerModel;
    this.listener = listener;
    initGUI();
    addListeners();
  }

  private void initGUI() {
    checkBox = new CheckBox();
    text = new Text(layerModel.getName());
    container = new HBox(5, checkBox, text);

    checkBox.setSelected(layerModel.isChecked());
    checkBox.setPadding(new Insets(3, 0, 5, 5));
    checkBox.setUserData(this);

    container.setAlignment(Pos.CENTER_LEFT);
    container.setPrefWidth(25);
    getChildren().add(container);
  }

  private void addListeners() {
    this.setOnMouseEntered(getOnMouseEnteredListener());
    this.setOnMouseExited(getOnMouseExitedListener());
    this.setOnMouseClicked(getOnMouseClickedListener());

    checkBox.selectedProperty().addListener(getCheckBoxSelectionChangeListener());
  }

  public void select(boolean isRightClick, double x, double y) {
    isSelected = true;
    layerModel.setSelected(true);
    container.setBackground(AppParameters.SELECTED_LAYER_BG);
    listener.selectedLayerChanged(this, isRightClick, x, y);
  }

  public void unselect() {
    isSelected = false;
    layerModel.setSelected(false);
    container.setBackground(AppParameters.TRANSPARENT_BG);
  }

  private void onMouseEntered() {
    if (!isSelected)
      container.setBackground(AppParameters.HOVERED_LAYER_BG);
  }

  private void onMouseExited() {
    if (!isSelected)
      container.setBackground(AppParameters.TRANSPARENT_BG);
  }

  public void setLayerChecked(boolean value) {
    layerModel.setChecked(value);
    listener.checkedLayerChanged(layerModel);
  }

  private static EventHandler<MouseEvent> getOnMouseEnteredListener() {
    if (onMouseEnteredListener == null) {
      onMouseEnteredListener = event -> {
        SelectableLayerView source = (SelectableLayerView) event.getSource();
        source.onMouseEntered();
      };
    }
    return onMouseEnteredListener;
  }

  private static EventHandler<MouseEvent> getOnMouseExitedListener() {
    if (onMouseExitedListener == null) {
      onMouseExitedListener = event -> {
        SelectableLayerView source = (SelectableLayerView) event.getSource();
        source.onMouseExited();
      };
    }
    return onMouseExitedListener;
  }

  public static EventHandler<MouseEvent> getOnMouseClickedListener() {
    if (onMouseClickedListener == null) {
      onMouseClickedListener = event -> {
        SelectableLayerView source = (SelectableLayerView) event.getSource();
        source.select(event.getButton() == MouseButton.SECONDARY, event.getSceneX(), event.getScreenY());
      };
    }
    return onMouseClickedListener;
  }

  private static ChangeListener<Boolean> getCheckBoxSelectionChangeListener() {
    if (checkBoxSelectionListener == null) {
      checkBoxSelectionListener = (observable, oldValue, newValue) -> {
        BooleanProperty booleanProperty = (BooleanProperty) observable;
        if (booleanProperty.getBean() instanceof CheckBox) {
          CheckBox checkBox = (CheckBox) booleanProperty.getBean();
          if (checkBox.getUserData() instanceof SelectableLayerView)
            ((SelectableLayerView) checkBox.getUserData()).setLayerChecked(newValue);
        }
      };
    }
    return checkBoxSelectionListener;
  }

  public LayerModel getLayerModel() {
    return layerModel;
  }

  public void updateModel(LayerModel layerModel) {
    this.layerModel = layerModel;
    text.setText(layerModel.getName());
  }
}

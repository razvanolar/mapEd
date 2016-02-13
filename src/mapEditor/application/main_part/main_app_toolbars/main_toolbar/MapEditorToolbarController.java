package mapEditor.application.main_part.main_app_toolbars.main_toolbar;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class MapEditorToolbarController implements Controller {

  public interface IMapEditorToolbarView extends View {
    ToggleButton getMapEditorViewButton();
    ToggleButton getImageEditorViewButton();
    ToggleGroup getToggleGroup();
  }

  private IMapEditorToolbarView view;

  public MapEditorToolbarController(IMapEditorToolbarView view) {
    this.view = view;
  }

  public void bind() {
    addListeners();
  }

  private void addListeners() {
    ChangeListener<Boolean> changeListener = (observable, oldValue, newValue) -> {
      if (newValue)
        MapEditorController.getInstance().changeView();
    };
    view.getMapEditorViewButton().selectedProperty().addListener(changeListener);
    view.getImageEditorViewButton().selectedProperty().addListener(changeListener);
  }

  public void changeToImageEditorView() {
    view.getImageEditorViewButton().setSelected(true);
  }

  public boolean isMapViewSelected() {
    return view.getMapEditorViewButton().isSelected();
  }
}

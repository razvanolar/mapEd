package mapEditor.application.main_app_toolbars.main_toolbar;

import javafx.scene.control.ToggleButton;
import mapEditor.MapEditorController;
import mapEditor.application.types.Controller;
import mapEditor.application.types.View;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class MapEditorToolbarController implements Controller {

  public interface IMapEditorToolbarView extends View {
    ToggleButton getMapEditorViewButton();
    ToggleButton getImageEditorViewButton();
  }

  private IMapEditorToolbarView view;

  public MapEditorToolbarController(IMapEditorToolbarView view) {
    this.view = view;
  }

  public void bind() {
    addListeners();
  }

  private void addListeners() {
    view.getMapEditorViewButton().setOnAction(event -> MapEditorController.getInstance().changeView());
    view.getImageEditorViewButton().setOnAction(event -> MapEditorController.getInstance().changeView());
  }

  public boolean isMapViewSelected() {
    return view.getMapEditorViewButton().isSelected();
  }
}

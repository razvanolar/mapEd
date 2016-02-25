package mapEditor.application.main_part.main_app_toolbars.main_toolbar;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.main_app_toolbars.main_toolbar.create_maps.CreateMapController;
import mapEditor.application.main_part.main_app_toolbars.main_toolbar.create_maps.CreateMapView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class MapEditorToolbarController implements Controller {

  public interface IMapEditorToolbarView extends View {
    Button getNewMapButton();
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
    ChangeListener<Boolean> changeListener = (observable, oldValue, newValue) -> {
      if (newValue)
        MapEditorController.getInstance().changeView();
    };
    view.getMapEditorViewButton().selectedProperty().addListener(changeListener);
    view.getImageEditorViewButton().selectedProperty().addListener(changeListener);

    view.getNewMapButton().setOnAction(event -> onNewMapButtonSelection());
  }

  /**
   * Called when creating a new map.
   */
  private void onNewMapButtonSelection() {
    OkCancelDialog dialog = new OkCancelDialog("Add New Map", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true);

    CreateMapController.ICreateMapView createMapView = new CreateMapView();
    CreateMapController createMapController = new CreateMapController(createMapView);
    createMapController.bind();

    dialog.getOkButton().setOnAction(event -> {

    });
    dialog.setContent(createMapView.asNode());
    dialog.show();
  }

  public void changeToImageEditorView() {
    view.getImageEditorViewButton().setSelected(true);
  }

  public boolean isMapViewSelected() {
    return view.getMapEditorViewButton().isSelected();
  }

  public void showCreateMapDialog() {
    onNewMapButtonSelection();
  }
}

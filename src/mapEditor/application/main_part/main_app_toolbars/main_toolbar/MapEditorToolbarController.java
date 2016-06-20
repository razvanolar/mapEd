package mapEditor.application.main_part.main_app_toolbars.main_toolbar;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.MapDetail;
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
    Button getTmxFormatButton();
    ToggleButton getChange2DVisibility();
    ToggleButton getChangeGridVisibility();
    ToggleButton getDeleteEntityButton();
    ToggleButton getFillAreaButton();
    ToggleButton getShowGridButton();
    ToggleButton getMapEditorViewButton();
    ToggleButton getImageEditorViewButton();
  }

  private IMapEditorToolbarView view;

  public MapEditorToolbarController(IMapEditorToolbarView view,
                                    boolean is2DVisibilitySelected,
                                    boolean isGridVisibilitySelected,
                                    boolean isDeleteEntity,
                                    boolean isFillArea,
                                    boolean isShowGridSelected) {
    this.view = view;
    this.view.getChange2DVisibility().setSelected(is2DVisibilitySelected);
    this.view.getChangeGridVisibility().setSelected(isGridVisibilitySelected);
    this.view.getDeleteEntityButton().setSelected(isDeleteEntity);
    this.view.getFillAreaButton().setSelected(isFillArea);
    this.view.getShowGridButton().setSelected(isShowGridSelected);
  }

  public void bind() {
    addListeners();
  }

  private void addListeners() {
    // new map listener
    view.getNewMapButton().setOnAction(event -> onNewMapButtonSelection());

    // tmx format listener
    view.getTmxFormatButton().setOnAction(event -> MapEditorController.getInstance().convertCurrentMapToTMX());

    // visibility listener
    view.getChange2DVisibility().selectedProperty().addListener((observable, oldValue, newValue) -> {
      MapEditorController.getInstance().changeVisibilityState(newValue, false);
    });
    view.getChangeGridVisibility().selectedProperty().addListener((observable, oldValue, newValue) -> {
      MapEditorController.getInstance().changeVisibilityState(false, newValue);
    });

    // delete listener
    view.getDeleteEntityButton().selectedProperty().addListener((observable, oldValue, newValue) -> {
      MapEditorController.getInstance().setDeleteEntityValue(newValue);
      if (newValue)
        view.getFillAreaButton().setSelected(false);
    });

    // fill area listener
    view.getFillAreaButton().selectedProperty().addListener((observable, oldValue, newValue) -> {
      MapEditorController.getInstance().setFillAreaValue(newValue);
      if (newValue)
        view.getDeleteEntityButton().setSelected(false);
    });

    // show grid listener
    view.getShowGridButton().selectedProperty().addListener((observable, oldValue, newValue) -> {
      MapEditorController.getInstance().showMapGrid(newValue);
    });

    // editors listeners
    ChangeListener<Boolean> editorsChangeListener = (observable, oldValue, newValue) -> {
      if (newValue)
        MapEditorController.getInstance().changeMainView();
    };
    view.getMapEditorViewButton().selectedProperty().addListener(editorsChangeListener);
    view.getImageEditorViewButton().selectedProperty().addListener(editorsChangeListener);
  }

  /**
   * Called when creating a new map.
   */
  private void onNewMapButtonSelection() {
    OkCancelDialog dialog = new OkCancelDialog("Add New Map", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true);

    CreateMapController.ICreateMapView createMapView = new CreateMapView();
    CreateMapController createMapController = new CreateMapController(createMapView, dialog.getOkButton(), AppParameters.CURRENT_PROJECT.getMapsFile());
    createMapController.bind();

    dialog.getOkButton().setOnAction(event -> {
      MapDetail mapDetail = createMapController.getMapDetailsModel();
      dialog.close();
      MapEditorController.getInstance().createNewMap(mapDetail);
    });
    dialog.setContent(createMapView.asNode());
    dialog.show();
  }

  public void changeToMapView() {
    view.getMapEditorViewButton().setSelected(true);
  }

  public void changeToImageEditorView() {
    view.getImageEditorViewButton().setSelected(true);
  }

  public boolean isMapViewSelected() {
    return view.getMapEditorViewButton().isSelected();
  }

  public boolean is2DVisibilitySelected() {
    return view.getChange2DVisibility().isSelected();
  }

  public boolean isGridVisibilitySelected() {
    return view.getChangeGridVisibility().isSelected();
  }

  public void showCreateMapDialog() {
    onNewMapButtonSelection();
  }
}

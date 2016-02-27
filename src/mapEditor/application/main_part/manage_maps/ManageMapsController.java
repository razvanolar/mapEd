package mapEditor.application.main_part.manage_maps;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.MapModel;
import mapEditor.application.main_part.manage_maps.layers.LayersController;
import mapEditor.application.main_part.manage_maps.manage_tiles.ManageTilesController;
import mapEditor.application.main_part.manage_maps.primary_map.PrimaryMapController;
import mapEditor.application.main_part.manage_maps.primary_map.PrimaryMapView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.SystemParameters;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class ManageMapsController implements Controller {

  public interface IMangeMapsView extends View {
    ScrollPane addMap(String title, PrimaryMapView mapView);
    TabPane getMapsTabPane();
    LayersController.ILayersView getLayersView();
    ManageTilesController.IManageTilesView getManageTilesView();
  }

  private IMangeMapsView view;
  private LayersController layersController;
  private ManageTilesController manageTilesController;
  private MapModel defaultModel;

  public ManageMapsController(IMangeMapsView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    defaultModel = new MapModel(SystemParameters.UNTITLED_MAP_TAB, "", "", SystemParameters.MAP_DEFAULT_SIZE_NUMBER,
            SystemParameters.MAP_DEFAULT_SIZE_NUMBER, SystemParameters.MAP_DEFAULT_BG_COLOR,
            SystemParameters.MAP_DEFAULT_GRID_COLOR, SystemParameters.MAP_DEFAULT_SQUARE_COLOR,
            AppParameters.CURRENT_PROJECT.getMapType());
    addListeners();
    initControllers();
    createMap(defaultModel, false);
  }

  private void addListeners() {
    view.getMapsTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
      if (newItem != null) {
        ScrollPane scrollPane = (ScrollPane) newItem.getContent();
        PrimaryMapView mapView = (PrimaryMapView) newItem.getUserData();
        if (scrollPane.getUserData() != null) {
          ChangeListener listener = (ChangeListener) scrollPane.getUserData();
          scrollPane.widthProperty().addListener(listener);
          scrollPane.heightProperty().addListener(listener);
          mapView.widthProperty().bind(scrollPane.widthProperty());
          mapView.heightProperty().bind(scrollPane.heightProperty());
        }
        mapView.paint();
      } else {
        createMap(defaultModel, false);
      }

      if (oldItem != null) {
        ScrollPane scrollPane = (ScrollPane) oldItem.getContent();
        PrimaryMapView mapView = (PrimaryMapView) oldItem.getUserData();
        ChangeListener listener = (ChangeListener) scrollPane.getUserData();
        scrollPane.widthProperty().removeListener(listener);
        scrollPane.widthProperty().removeListener(listener);
        mapView.widthProperty().unbind();
      }
    });
  }

  private void initControllers() {
    layersController = new LayersController(view.getLayersView());
    layersController.bind();

    manageTilesController = new ManageTilesController(view.getManageTilesView());
    manageTilesController.bind();
  }

  private void createMap(MapModel mapModel, boolean removeUntitled) {
    PrimaryMapView primaryMapView = new PrimaryMapView(mapModel);
    ScrollPane scrollPane = view.addMap(mapModel.getName(), primaryMapView);
    PrimaryMapController controller = new PrimaryMapController(primaryMapView, scrollPane);

    ChangeListener<Number> sizeChangeListener = (observable, oldValue, newValue) -> primaryMapView.paint();

    scrollPane.setUserData(sizeChangeListener);
    scrollPane.widthProperty().addListener(sizeChangeListener);
    scrollPane.heightProperty().addListener(sizeChangeListener);

    controller.bind();

    if (mapModel != defaultModel)
      AppParameters.CURRENT_PROJECT.addMapModel(mapModel);

    if (removeUntitled)
      removeUntitledTab();
  }

  /**
   * Checks to see if there is an SystemParameters.UNTITLED_MAP_TAB in the tab pane items.
   * If there is such a tab, it will be removed.
   */
  private void removeUntitledTab() {
    Tab untitledTab = null;
    for (Tab tab : view.getMapsTabPane().getTabs())
      if (tab.getText().equals(SystemParameters.UNTITLED_MAP_TAB)) {
        untitledTab = tab;
        break;
      }
    if (untitledTab != null)
      view.getMapsTabPane().getTabs().remove(untitledTab);
  }

  public void addNewMap(MapModel mapModel) {
    createMap(mapModel, true);
  }

  public View getView() {
    return view;
  }
}


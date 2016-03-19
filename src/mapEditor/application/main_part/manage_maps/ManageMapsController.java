package mapEditor.application.main_part.manage_maps;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.LayerModel;
import mapEditor.application.main_part.app_utils.models.MapDetail;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.manage_maps.manage_layers.LayersController;
import mapEditor.application.main_part.manage_maps.manage_tiles.ManageTilesController;
import mapEditor.application.main_part.manage_maps.visibility_map.MapVisibilityController;
import mapEditor.application.main_part.manage_maps.visibility_map.MapVisibilityView;
import mapEditor.application.main_part.manage_maps.primary_map.PrimaryMapController;
import mapEditor.application.main_part.manage_maps.primary_map.PrimaryMapView;
import mapEditor.application.main_part.manage_maps.utils.MapContentStateKeys;
import mapEditor.application.main_part.manage_maps.utils.MapLayersListener;
import mapEditor.application.main_part.manage_maps.utils.SelectedTileListener;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.SystemParameters;

import java.io.File;
import java.util.List;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class ManageMapsController implements Controller, MapLayersListener, SelectedTileListener {

  public interface IMangeMapsView extends View {
    ScrollPane addMap(String title, PrimaryMapView mapView, boolean selectTab);
    TabPane getMapsTabPane();
    LayersController.ILayersView getLayersView();
    ManageTilesController.IManageTilesView getManageTilesView();
  }

  private IMangeMapsView view;
  private LayersController layersController;
  private ManageTilesController manageTilesController;
  private MapDetail defaultModel;

  private ImageModel selectedTile;

  public ManageMapsController(IMangeMapsView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    defaultModel = new MapDetail(SystemParameters.UNTITLED_MAP_TAB, "", "", SystemParameters.MAP_DEFAULT_SIZE_NUMBER,
            SystemParameters.MAP_DEFAULT_SIZE_NUMBER, SystemParameters.MAP_DEFAULT_BG_COLOR,
            SystemParameters.MAP_DEFAULT_GRID_COLOR, SystemParameters.MAP_DEFAULT_SQUARE_COLOR,
            AppParameters.CURRENT_PROJECT.getMapType());

    initControllers();
    List<MapDetail> mapDetails = AppParameters.CURRENT_PROJECT.getMapDetails();
    if (mapDetails != null && !mapDetails.isEmpty()) {
      for (MapDetail mapDetail : mapDetails)
        createMap(mapDetail, false, mapDetail.isSelected());
    } else
      createMap(defaultModel, false, false);

    addListeners();
    change2DVisibilityState(MapEditorController.getInstance().is2DVisibilitySelected(), getSelectedTab());
  }

  private void addListeners() {
    view.getMapsTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
      if (newItem != null) {
        boolean is2DVisibilitySelected = MapEditorController.getInstance().is2DVisibilitySelected();
        MapCanvas mapCanvas = (MapCanvas) newItem.getUserData();
        MapCanvas visibilityCanvas = null;

        // update map details fields before changing the view
        MapDetail mapDetail = mapCanvas.getMapDetail();
        mapDetail.setSelected(true);
        layersController.loadLayers(mapDetail.getLayers());
        mapCanvas.setDrawingTile(selectedTile);

        // change view according to selected visibility state
        change2DVisibilityState(is2DVisibilitySelected, newItem);

        // add scroll pane size change listeners
        ScrollPane scrollPane = (ScrollPane) newItem.getContent();
        if (scrollPane.getUserData() != null && scrollPane.getUserData() instanceof ChangeListener) {
          ChangeListener listener = (ChangeListener) scrollPane.getUserData();
          scrollPane.widthProperty().addListener(listener);
          scrollPane.heightProperty().addListener(listener);
        }

        // bind canvas size properties with the scroll pane properties
        if (is2DVisibilitySelected) {
          Object object = newItem.getProperties().get(MapContentStateKeys.VISIBILITY_CONTROLLER);
          if (object != null && object instanceof MapVisibilityController) {
            MapVisibilityController visibilityController = (MapVisibilityController) object;
            visibilityCanvas = visibilityController.getShadowMap();
            visibilityCanvas.widthProperty().bind(scrollPane.widthProperty());
            visibilityCanvas.heightProperty().bind(scrollPane.heightProperty());
            visibilityCanvas.paint();
          }
        } else {
          mapCanvas.widthProperty().bind(scrollPane.widthProperty());
          mapCanvas.heightProperty().bind(scrollPane.heightProperty());
          mapCanvas.paint();
        }
      } else {
        createMap(defaultModel, false, false);
      }

      if (oldItem != null) {
        ScrollPane scrollPane = (ScrollPane) oldItem.getContent();
        PrimaryMapView mapView = (PrimaryMapView) oldItem.getUserData();

        // unbind and remove listeners for primary map
        ChangeListener listener = (ChangeListener) scrollPane.getUserData();
        scrollPane.widthProperty().removeListener(listener);
        scrollPane.widthProperty().removeListener(listener);
        mapView.widthProperty().unbind();
        mapView.heightProperty().unbind();
        mapView.getMapDetail().setSelected(false);

        // if there is a visibility map created, make sure to unbind that also
        Object object = oldItem.getProperties().get(MapContentStateKeys.VISIBILITY_CONTROLLER);
        if (object != null && object instanceof MapVisibilityController) {
          ((MapVisibilityController) object).unbindMap();
        }
      }
    });

    view.getMapsTabPane().getTabs().addListener((ListChangeListener<Tab>) c -> {
      while (c.next()) {
        if (c.getRemovedSize() > 0) {
          for (Tab tab : c.getRemoved()) {
            PrimaryMapView mapView = (PrimaryMapView) tab.getUserData();
            AppParameters.CURRENT_PROJECT.removeMapModel(mapView.getMapDetail());
          }
        }
      }
    });
  }

  private void initControllers() {
    layersController = new LayersController(view.getLayersView(), this);
    layersController.bind();

    manageTilesController = new ManageTilesController(view.getManageTilesView(), this);
    manageTilesController.bind();
  }

  private void createMap(MapDetail mapDetail, boolean removeUntitled, boolean selectTab) {
    mapDetail.setShowGrid(AppParameters.CURRENT_PROJECT.isShowGrid());
    PrimaryMapView primaryMapView = new PrimaryMapView(mapDetail);
    ScrollPane scrollPane = view.addMap(mapDetail.getName(), primaryMapView, selectTab);
    PrimaryMapController controller = new PrimaryMapController(primaryMapView, scrollPane);

    ChangeListener<Number> sizeChangeListener = (observable, oldValue, newValue) -> primaryMapView.paint();

    scrollPane.setUserData(sizeChangeListener);
    scrollPane.widthProperty().addListener(sizeChangeListener);
    scrollPane.heightProperty().addListener(sizeChangeListener);

    controller.bind();

    if (mapDetail != defaultModel)
      AppParameters.CURRENT_PROJECT.addMapModel(mapDetail);
    if (selectTab)
      layersController.loadLayers(mapDetail.getLayers());

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

  /**
   * Open the existing map file into edit mode. Usually called from the project tree controller.
   * First, check to see if the specified map is already opened. If it is, set as selected, otherwise,
   * open it from the disk.
   * @param file
   * Selected map file.
   */
  public void openMap(File file) {
    if (file == null)
      return;

    // select tab if opened
    Tab mapTab = getTabForMapIfOpened(file);
    if (mapTab != null) {
      view.getMapsTabPane().getSelectionModel().select(mapTab);
      return;
    }

    // otherwise, load the map from disk
    try {
      MapEditorController.getInstance().maskView();
      MapDetail mapDetail = MapEditorController.getInstance().getRepoController().createMapModelFromFile(AppParameters.CURRENT_PROJECT.getHomePath(), file, null);
      MapEditorController.getInstance().unmaskView();
      if (mapDetail != null)
        createMap(mapDetail, true, true);
      else
        Dialog.showWarningDialog("ManageMapsController - Warning", "Map instance is null.");
    } catch (Exception ex) {
      ex.printStackTrace();
      MapEditorController.getInstance().unmaskView();
      Dialog.showErrorDialog("ManageMapsController - Error", "Error occurred while loading the map. Message: " + ex.getMessage());
    }
  }

  public void exportMapToHtml(File file) {
    if (file == null)
      return;

    Tab mapTab = getTabForMapIfOpened(file);
    MapEditorController.getInstance().maskView();
    try {
      // if the map is opened, save it first
      if (mapTab != null && mapTab.getUserData() != null && mapTab.getUserData() instanceof PrimaryMapView) {
        PrimaryMapView mapView = (PrimaryMapView) mapTab.getUserData();
        mapView.updateMapModelDetails();
        mapView.updateMapModelInfos();
        MapEditorController.getInstance().getRepoController().saveMap(AppParameters.CURRENT_PROJECT.getHomePath(),
                mapView.getMapDetail(), null, true);
        System.out.println("map saved");
      }

      MapEditorController.getInstance().getRepoController().exportMapToHtml(AppParameters.CURRENT_PROJECT.getHomePath(), file);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    MapEditorController.getInstance().unmaskView();
  }

  public void change2DVisibilityState(boolean is2DVisibilitySelected, Tab tab) {
    if (tab == null)
      tab = view.getMapsTabPane().getSelectionModel().getSelectedItem();
    if (tab == null || tab.getUserData() == null || !(tab.getUserData() instanceof PrimaryMapView))
      return;

    PrimaryMapView mapView = (PrimaryMapView) tab.getUserData();

    MapVisibilityController visibilityController = null;
    Object object = tab.getProperties().get(MapContentStateKeys.VISIBILITY_CONTROLLER);
    if (object != null && object instanceof MapVisibilityController)
      visibilityController = (MapVisibilityController) object;

    mapView.updateMapModelDetails();
    mapView.updateMapModelInfos();

    if (is2DVisibilitySelected) {
      if (visibilityController == null) {
        MapVisibilityController.IMapVisibilityView visibilityView = new MapVisibilityView();
        visibilityController = new MapVisibilityController(visibilityView, mapView.getMapDetail().duplicate());
        visibilityController.bind();

        tab.setContent(visibilityView.asNode());
        tab.getProperties().put(MapContentStateKeys.VISIBILITY_CONTROLLER, visibilityController);
      } else {
        visibilityController.setMapDetail(mapView.getMapDetail().duplicate());
        tab.setContent(visibilityController.getView().asNode());
      }
      mapView.widthProperty().unbind();
      mapView.heightProperty().unbind();
    } else {
      object = tab.getProperties().get(MapContentStateKeys.MAP_SCROLL_PANE);
      if (object != null && object instanceof ScrollPane) {
        ScrollPane scrollPane = (ScrollPane) object;
        tab.setContent(scrollPane);
        mapView.widthProperty().bind(scrollPane.widthProperty());
        mapView.heightProperty().bind(scrollPane.heightProperty());
        if (visibilityController != null)
          visibilityController.unbindMap();
        mapView.paint();
      }
    }
  }

  /**
   * Add a new map in the tab pane. Called when creating a new map.
   * @param mapDetail
   * MapDetail
   */
  public void addNewMap(MapDetail mapDetail) {
    createMap(mapDetail, true, true);
  }

  public void updateMapModelsForExistingTabs() {
    for (Tab tab : view.getMapsTabPane().getTabs()) {
      PrimaryMapView mapView = (PrimaryMapView) tab.getUserData();
      mapView.updateMapModelDetails();
      mapView.updateMapModelInfos();
    }
  }

  public void showGridValueChanged(boolean value) {
    for (Tab tab : view.getMapsTabPane().getTabs()) {
      PrimaryMapView mapView = (PrimaryMapView) tab.getUserData();
      mapView.setEnableGrid(value);
      if (tab.isSelected())
        mapView.paint();
    }
  }

  @Override
  public void addLayer(LayerModel layer) {
    PrimaryMapView mapView = getSelectedMap();
    mapView.getMapDetail().addLayer(layer);
  }

  @Override
  public void removeLayer(LayerModel layer) {
    PrimaryMapView mapView = getSelectedMap();
    mapView.getMapDetail().removeLayer(layer);
    mapView.paint();
  }

  @Override
  public void moveLayerUp(LayerModel layer) {
    PrimaryMapView mapView = getSelectedMap();
    List<LayerModel> layers = mapView.getMapDetail().getLayers();
    if (layer == null)
      return;
    int index = layers.indexOf(layer);
    if (index <= 0)
      return;
    layers.remove(index);
    layers.add(index - 1, layer);
    mapView.paint();
  }

  @Override
  public void moveLayerDown(LayerModel layer) {
    PrimaryMapView mapView = getSelectedMap();
    List<LayerModel> layers = mapView.getMapDetail().getLayers();
    if (layers == null)
      return;
    int index = layers.indexOf(layer);
    if (index == -1 || index == layers.size() - 1)
      return;
    layers.remove(index);
    layers.add(index + 1, layer);
    mapView.paint();
  }

  @Override
  public void selectedLayerChanged(LayerModel layer) {
    PrimaryMapView selectedMap = getSelectedMap();
    if (selectedMap != null && selectedMap.getMapDetail().getLayers().contains(layer))
      selectedMap.setCurrentLayer(layer);
  }

  @Override
  public void checkedLayerChanged(LayerModel layer) {
    boolean is2DVisibilitySelected = MapEditorController.getInstance().is2DVisibilitySelected();
    if (is2DVisibilitySelected) {
      Tab tab = view.getMapsTabPane().getSelectionModel().getSelectedItem();
      if (tab == null)
        return;
      Object object = tab.getProperties().get(MapContentStateKeys.VISIBILITY_CONTROLLER);
      if (object != null && object instanceof MapVisibilityController) {
        ((MapVisibilityController) object).update();
      }
    } else {
      PrimaryMapView selectedMap = getSelectedMap();
      if (selectedMap != null && selectedMap.getMapDetail().getLayers().contains(layer))
        selectedMap.paint();
    }
  }

  @Override
  public void selectedTileChanged(ImageModel selectedTile) {
    this.selectedTile = selectedTile;
    PrimaryMapView selectedMap = getSelectedMap();
    if (selectedMap != null)
      selectedMap.setDrawingTile(selectedTile);
  }

  /**
   * Return the corresponding tab for the specified map file if it's opened.
   * @param file map file
   * @return map tab, if opened; null otherwise
   */
  private Tab getTabForMapIfOpened(File file) {
    if (file == null)
      return null;
    String filePath = file.getParentFile().getAbsolutePath();
    String fileName = file.getName();
    filePath = filePath.endsWith("\\") ? filePath : filePath + "\\";

    for (Tab tab : view.getMapsTabPane().getTabs()) {
      PrimaryMapView mapView = (PrimaryMapView) tab.getUserData();
      MapDetail mapDetail = mapView.getMapDetail();
      String mapPath = mapDetail.getAbsolutePath();
      mapPath = mapPath.endsWith("\\") ? mapPath : mapPath + "\\";
      if (mapPath.equals(filePath) && mapDetail.getName().equals(fileName)) {
        return tab;
      }
    }
    return null;
  }

  /**
   * Gets the selected map model. The data model will be updated.
   * @return MapDetail
   */
  public MapDetail getSelectedMapDetail() {
    PrimaryMapView mapView = getSelectedMap();
    if (mapView == null)
      return null;
    mapView.updateMapModelDetails();
    mapView.updateMapModelInfos();
    return mapView.getMapDetail();
  }

  private PrimaryMapView getSelectedMap() {
    return (PrimaryMapView) view.getMapsTabPane().getSelectionModel().getSelectedItem().getUserData();
  }

  private Tab getSelectedTab() {
    return view.getMapsTabPane().getSelectionModel().getSelectedItem();
  }

  public View getView() {
    return view;
  }

  public ManageTilesController getManageTilesController() {
    return manageTilesController;
  }
}


package mapEditor.application.main_part.manage_maps;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.*;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.manage_maps.manage_layers.LayersController;
import mapEditor.application.main_part.manage_maps.manage_tiles.ManageTilesController;
import mapEditor.application.main_part.manage_maps.primary_map.context_menu.PrimaryMapContextMenuController;
import mapEditor.application.main_part.manage_maps.primary_map.context_menu.PrimaryMapContextMenuView;
import mapEditor.application.main_part.manage_maps.primary_map.tmx_export.TmxExportController;
import mapEditor.application.main_part.manage_maps.primary_map.tmx_export.TmxExportView;
import mapEditor.application.main_part.manage_maps.utils.RenameMapController;
import mapEditor.application.main_part.manage_maps.utils.listeners.MapContextMenuListener;
import mapEditor.application.main_part.manage_maps.visibility_map.Map2DVisibilityController;
import mapEditor.application.main_part.manage_maps.visibility_map.Map2DVisibilityView;
import mapEditor.application.main_part.manage_maps.primary_map.PrimaryMapController;
import mapEditor.application.main_part.manage_maps.primary_map.PrimaryMapView;
import mapEditor.application.main_part.manage_maps.utils.MapContentStateKeys;
import mapEditor.application.main_part.manage_maps.utils.listeners.MapLayersListener;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectedTileListener;
import mapEditor.application.main_part.manage_maps.visibility_map.MapGridVisibilityController;
import mapEditor.application.main_part.manage_maps.visibility_map.MapGridVisibilityView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.SystemParameters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class ManageMapsController implements Controller, MapLayersListener, SelectedTileListener, MapContextMenuListener {

  public interface IMangeMapsView extends View {
    ScrollPane addMap(String title, PrimaryMapView mapView, boolean selectTab);
    TabPane getMapsTabPane();
    LayersController.ILayersView getLayersView();
    ManageTilesController.IManageTilesView getManageTilesView();
    double getDividerPosition();
    void setDividerPosition(double value);
  }

  private IMangeMapsView view;
  private LayersController layersController;
  private ManageTilesController manageTilesController;
  private PrimaryMapContextMenuController contextMenuController;
  private MapDetail defaultModel;

  private AbstractDrawModel selectedTile;
  private boolean blockTabListener;

  public ManageMapsController(IMangeMapsView view, double dividerPosition) {
    this.view = view;
//    this.view.setDividerPosition(dividerPosition);
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
    change2DVisibilityState(MapEditorController.getInstance().is2DVisibilitySelected(),
            MapEditorController.getInstance().isGridVisibilitySelected(),
            getSelectedTab());
  }

  private void addListeners() {
    view.getMapsTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
      if (blockTabListener)
        return;
      if (newItem != null) {
        boolean is2DVisibilitySelected = MapEditorController.getInstance().is2DVisibilitySelected();
        boolean isGridVisibilitySelected = MapEditorController.getInstance().isGridVisibilitySelected();
        MapCanvas mapCanvas = (MapCanvas) newItem.getUserData();
        MapCanvas visibilityCanvas = null;

        // update map details fields before changing the view
        MapDetail mapDetail = mapCanvas.getMapDetail();
        mapDetail.setSelected(true);
        layersController.loadLayers(mapDetail.getLayers());
        mapCanvas.setDrawingTile(selectedTile);

        // change view according to selected visibility state
        change2DVisibilityState(is2DVisibilitySelected, isGridVisibilitySelected, newItem);

        // add scroll pane size change listeners
        ScrollPane scrollPane = (ScrollPane) newItem.getContent();
        if (scrollPane.getUserData() != null && scrollPane.getUserData() instanceof ChangeListener) {
          ChangeListener listener = (ChangeListener) scrollPane.getUserData();
          scrollPane.widthProperty().addListener(listener);
          scrollPane.heightProperty().addListener(listener);
        }

        // bind canvas size properties with the scroll pane properties
        if (is2DVisibilitySelected) {
          Object object = newItem.getProperties().get(MapContentStateKeys.VISIBILITY_2D_CONTROLLER);
          if (object != null && object instanceof Map2DVisibilityController) {
            Map2DVisibilityController visibilityController = (Map2DVisibilityController) object;
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
        Object object = oldItem.getProperties().get(MapContentStateKeys.VISIBILITY_2D_CONTROLLER);
        if (object != null && object instanceof Map2DVisibilityController) {
          ((Map2DVisibilityController) object).unbindMap();
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

    contextMenuController = new PrimaryMapContextMenuController(new PrimaryMapContextMenuView(), this);
    contextMenuController.bind();
  }

  private void createMap(MapDetail mapDetail, boolean removeUntitled, boolean selectTab) {
    blockTabListener = true;
    try {
      mapDetail.setShowGrid(AppParameters.CURRENT_PROJECT.isShowGrid());
      PrimaryMapView primaryMapView = new PrimaryMapView(mapDetail);
      ScrollPane scrollPane = view.addMap(mapDetail.getName(), primaryMapView, selectTab);
      PrimaryMapController controller = new PrimaryMapController(primaryMapView, scrollPane, contextMenuController);

      ChangeListener<Number> sizeChangeListener = (observable, oldValue, newValue) -> primaryMapView.paint();

      scrollPane.setUserData(sizeChangeListener);
      scrollPane.widthProperty().addListener(sizeChangeListener);
      scrollPane.heightProperty().addListener(sizeChangeListener);

      controller.bind();

      if (mapDetail != defaultModel)
        AppParameters.CURRENT_PROJECT.addMapModel(mapDetail);
      if (selectTab) {
        layersController.loadLayers(mapDetail.getLayers());
        mapDetail.setSelected(true);
      }

      if (removeUntitled)
        removeUntitledTab();
    } catch (Exception ex) {
      Dialog.showConfirmDialog(null, "Unable to create map. Error message: " + ex.getMessage());
    }
    blockTabListener = false;
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
   * Checks to see if there is a map with same absolute path as of the specified file, and deletes it.
   * @param mapFile
   * Map file
   */
  private void removeMapTabByFile(File mapFile) {
    if (mapFile == null)
      return;
    String mapAbsPath = mapFile.getParentFile().getAbsolutePath();
    String mapName = mapFile.getName();
    mapAbsPath = !mapAbsPath.endsWith("\\") ? mapAbsPath + "\\" : mapAbsPath;

    PrimaryMapView mapView;
    Tab tabToDelete = null;
    for (Tab tab : view.getMapsTabPane().getTabs()) {
      if (tab.getUserData() != null && tab.getUserData() instanceof PrimaryMapView) {
        mapView = (PrimaryMapView) tab.getUserData();
        MapDetail mapDetail = mapView.getMapDetail();
        if (mapDetail.getAbsolutePath() != null &&
                mapDetail.getAbsolutePath().equalsIgnoreCase(mapAbsPath) &&
                mapDetail.getName().equals(mapName)) {
          tabToDelete = tab;
          break;
        }
      }
    }
    if (tabToDelete != null) {
      view.getMapsTabPane().getTabs().remove(tabToDelete);
    }
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
      if (mapDetail != null) {
        createMap(mapDetail, true, true);
        change2DVisibilityState(AppParameters.CURRENT_PROJECT.is2DVisibilitySelected(),
                AppParameters.CURRENT_PROJECT.isGridVisibilitySelected(),
                getSelectedTab());
      } else
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

  public void change2DVisibilityState(boolean is2DVisibilitySelected, boolean isGridVisibilitySelected, Tab tab) {
    if (tab == null)
      tab = view.getMapsTabPane().getSelectionModel().getSelectedItem();
    if (is2DVisibilitySelected && isGridVisibilitySelected) {
      Dialog.showWarningDialog(null, "Both 2D and grid visibility state are selected!");
      return;
    }
    if (tab == null || tab.getUserData() == null || !(tab.getUserData() instanceof PrimaryMapView))
      return;

    PrimaryMapView mapView = (PrimaryMapView) tab.getUserData();

    Map2DVisibilityController visibility2DController = null;
    MapGridVisibilityController visibilityGridController = null;
    Object visibilityController1 = tab.getProperties().get(MapContentStateKeys.VISIBILITY_2D_CONTROLLER);
    Object visibilityController2 = tab.getProperties().get(MapContentStateKeys.VISIBILITY_GRID_CONTROLLER);
    if (visibilityController1 != null && visibilityController1 instanceof Map2DVisibilityController)
      visibility2DController = (Map2DVisibilityController) visibilityController1;
    if (visibilityController2 != null && visibilityController2 instanceof MapGridVisibilityController)
      visibilityGridController = (MapGridVisibilityController) visibilityController2;

    mapView.updateMapModelDetails();
    mapView.updateMapModelInfos();

    if (is2DVisibilitySelected) {
      if (visibility2DController == null) {
        Map2DVisibilityController.IMap2DVisibilityView visibilityView = new Map2DVisibilityView();
        visibility2DController = new Map2DVisibilityController(visibilityView, mapView.getMapDetail().duplicate());
        visibility2DController.bind();

        tab.setContent(visibilityView.asNode());
        tab.getProperties().put(MapContentStateKeys.VISIBILITY_2D_CONTROLLER, visibility2DController);
      } else {
        visibility2DController.setMapDetail(mapView.getMapDetail().duplicate());
        tab.setContent(visibility2DController.getView().asNode());
      }
      mapView.widthProperty().unbind();
      mapView.heightProperty().unbind();
    } else if (isGridVisibilitySelected) {
      if (visibilityGridController == null) {
        MapGridVisibilityController.IMapGridVisibilityView visibilityView = new MapGridVisibilityView();
        visibilityGridController = new MapGridVisibilityController(visibilityView, mapView.getMapDetail().duplicate());
        visibilityGridController.bind();

        tab.setContent(visibilityView.asNode());
        tab.getProperties().put(MapContentStateKeys.VISIBILITY_GRID_CONTROLLER, visibilityGridController);
      } else {
        visibilityGridController.setMapDetail(mapView.getMapDetail().duplicate());
        tab.setContent(visibilityGridController.getView().asNode());
      }
    } else {
      Object object = tab.getProperties().get(MapContentStateKeys.MAP_SCROLL_PANE);
      if (object != null && object instanceof ScrollPane) {
        ScrollPane scrollPane = (ScrollPane) object;
        tab.setContent(scrollPane);
        mapView.widthProperty().bind(scrollPane.widthProperty());
        mapView.heightProperty().bind(scrollPane.heightProperty());
        if (visibility2DController != null)
          visibility2DController.unbindMap();
        mapView.paint();
      }
    }
  }

  public void exportCurrentMapToTMX() {
    PrimaryMapView mapView = getSelectedMap();
    if (mapView == null)
      return;

    mapView.updateMapModelDetails();
    mapView.updateMapModelInfos();

    OkCancelDialog dialog = new OkCancelDialog("Export map to .tmx format", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true);
    TmxExportController.ITmxExportView tmxView = new TmxExportView();
    TmxExportController tmxController = new TmxExportController(tmxView, mapView.getMapDetail(), dialog.getStage(), dialog.getOkButton());
    tmxController.bind();
    dialog.setContent(tmxView.asNode());

    dialog.getOkButton().setOnAction(event -> {
      try {
        // the ids of the tiles will be set when calling this method
        // when the distinct tiles are computed
        String tilesetName = tmxController.constructAndSaveTileset();
        boolean result = tilesetName != null;
        if (!result) {
          dialog.close();
          Dialog.showWarningDialog(null, "Unable to compute and save the tileset");
        }

        // export the map in tmx format
        result = tmxController.exportMapToTMX(tilesetName);
        dialog.close();
        if (result)
          Dialog.showInformDialog(null, "Tileset and the map in tmx format were saved");
        else
          Dialog.showWarningDialog(null, "Unable to save the map");
      } catch (Exception ex) {
        dialog.close();
        Dialog.showWarningDialog(null, ex.getMessage());
        ex.printStackTrace();
      }
    });

    dialog.show();
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
      Object object = tab.getProperties().get(MapContentStateKeys.VISIBILITY_2D_CONTROLLER);
      if (object != null && object instanceof Map2DVisibilityController) {
        ((Map2DVisibilityController) object).update();
      }
    } else {
      PrimaryMapView selectedMap = getSelectedMap();
      if (selectedMap != null && selectedMap.getMapDetail().getLayers().contains(layer))
        selectedMap.paint();
    }
  }

  @Override
  public void selectedTileChanged(AbstractDrawModel selectedTile) {
    this.selectedTile = selectedTile;
    PrimaryMapView selectedMap = getSelectedMap();
    if (selectedMap != null)
      selectedMap.setDrawingTile(selectedTile);
  }

  @Override
  public void saveCurrentMap() {
    MapEditorController.getInstance().saveSelectedMap();
  }

  @Override
  public void renameCurrentMap() {
    MapDetail mapDetail = getSelectedMapDetail();

    OkCancelDialog dialog = new OkCancelDialog("Rename Map", StageStyle.UTILITY, Modality.APPLICATION_MODAL, false, 5);
    RenameMapController renameController = new RenameMapController(mapDetail.getName(), dialog.getOkButton());
    renameController.bind();
    dialog.getOkButton().setOnAction(event -> {
      renameMap(mapDetail, renameController.getMapName());
      dialog.close();
    });
    dialog.setContent(renameController.getView());
    dialog.show();
  }

  @Override
  public void deleteCurrentMap() {
    if (Dialog.showYesNoDialog(null, "Are you sure you want to delete the map?")) {
      PrimaryMapView mapView = getSelectedMap();
      if (mapView == null)
        return;
      File mapFile = new File(mapView.getMapDetail().getAbsolutePath() + mapView.getMapDetail().getName());
      boolean mapWasDeleted = MapEditorController.getInstance().deleteMap(mapFile);
      if (mapWasDeleted)
        removeMapTabByFile(mapFile);
    }
  }

  /**
   * Rename the specified map. After renaming, try to save the map details.
   * @param mapDetail
   * MapDetail - details of the map that is renamed.
   * @param newName
   * String - new map name.
   */
  private void renameMap(MapDetail mapDetail, String newName) {
    try {
      String fromName = mapDetail.getAbsolutePath() + mapDetail.getName();
      String toName = mapDetail.getAbsolutePath() + newName;
      newName = MapEditorController.getInstance().getRepoController().renameFile(fromName, toName);
      if (newName == null) {
        Dialog.showWarningDialog(null, "Unable to rename map file: " + fromName);
        return;
      }
      String oldName = mapDetail.getName();
      mapDetail.setName(newName);
      String mapName = MapEditorController.getInstance().getRepoController().saveMap(AppParameters.CURRENT_PROJECT.getHomePath(),
              mapDetail, null, true);
      if (mapName == null) {
        mapDetail.setName(oldName);
        Dialog.showWarningDialog(null, "Unable to save map details after the map was renamed.");
        return;
      }
      renameTabByMapDetail(mapDetail);
    } catch (Exception ex) {
      Dialog.showErrorDialog(null, "Error occurred when renaming the map. Error message: " + ex.getMessage());
    }
  }

  private void renameTabByMapDetail(MapDetail mapDetail) {
    if (mapDetail == null || StringValidator.isNullOrEmpty(mapDetail.getName()))
      return;
    for (Tab tab : view.getMapsTabPane().getTabs()) {
      if (tab.getUserData() != null && tab.getUserData() instanceof PrimaryMapView) {
        PrimaryMapView mapView = (PrimaryMapView) tab.getUserData();
        MapDetail md = mapView.getMapDetail();
        if (md.getName().equals(mapDetail.getName()) && md.getName().equals(mapDetail.getAbsolutePath()) || md == mapDetail) {
          tab.setText(mapDetail.getName());
          break;
        }
      }
    }
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

  public double getDividerPosition() {
    return view.getDividerPosition();
  }

  public ManageTilesController getManageTilesController() {
    return manageTilesController;
  }
}


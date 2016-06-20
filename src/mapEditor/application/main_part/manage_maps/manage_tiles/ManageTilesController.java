package mapEditor.application.main_part.manage_maps.manage_tiles;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.TabKey;
import mapEditor.application.main_part.app_utils.models.brush.BrushTileModel;
import mapEditor.application.main_part.app_utils.models.object.ObjectModel;
import mapEditor.application.main_part.app_utils.models.object.ObjectTileModel;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.manage_maps.manage_tiles.create_tiles_tab.CreateTilesTabView;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.AbstractTabContainer;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.BrushesTabContainer;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.ObjectsTabContainer;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.TilesTabContainer;
import mapEditor.application.main_part.manage_maps.utils.*;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectableEntityListener;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectedEntityListener;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.app_utils.models.brush.BrushModel;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.models.ProjectModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * Created by razvanolar on 23.01.2016.
 */
public class ManageTilesController implements Controller, SelectableEntityListener {

  public interface IManageTilesView extends View {
    TabPane getTabPane();
    Button getNewTabButton();
    Button getAddTilesButton();
    void addTab(Tab tab);
  }

  private IManageTilesView view;
  private SelectableTileView selectedTileView;
  private SelectedEntityListener listener;

  public ManageTilesController(IManageTilesView view, SelectedEntityListener listener) {
    this.view = view;
    this.listener = listener;
  }

  public void bind() {
    CustomMap<TabKey, List<File>> tabs = AppParameters.CURRENT_PROJECT.getOpenedTileTabs();
    if (tabs != null && !tabs.isEmpty()) {
      for (TabKey key : tabs.keys()) {
        List<File> drawModels = tabs.get(key);
        if (drawModels == null || drawModels.isEmpty())
          continue;
        if (key.getType() == TabType.TILES)
          addTilesTabForFiles(key.getName(), drawModels);
        else if (key.getType() == TabType.BRUSHES)
          addBrushTabForBrushFiles(key.getName(), drawModels);
        else if (key.getType() == TabType.OBJECTS)
          addObjectTabForObjectFiles(key.getName(), drawModels);
      }
    }
    addListeners();
  }

  private void addListeners() {
    view.getNewTabButton().setOnAction(event -> onAddNewTabSelection());

    view.getTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
      onTabChanged(oldItem, newItem);
    });

    view.getTabPane().getTabs().addListener((ListChangeListener<Tab>) c -> {
      ProjectModel currentProject = AppParameters.CURRENT_PROJECT;
      while (c.next()) {

        List<? extends Tab> deleted = c.getRemoved();
        if (deleted != null && !deleted.isEmpty()) {
          deleted.stream().filter(tab -> tab.getUserData() != null).forEach(
                  tab -> currentProject.removeTileTabKey(((AbstractTabContainer) tab.getUserData()).getKey())
          );
        }

        List<? extends Tab> added = c.getAddedSubList();
        if (added != null && !added.isEmpty()) {
          for (Tab tab : added) {
            if (tab.getUserData() != null && tab.getUserData() instanceof AbstractTabContainer) {
              AbstractTabContainer abstractTab = (AbstractTabContainer) tab.getUserData();
              if (abstractTab.getTabType() == TabType.TILES && abstractTab instanceof TilesTabContainer) {
                TilesTabContainer tilesTab = (TilesTabContainer) abstractTab;
                for (ImageModel tile : tilesTab.getTileModels())
                  currentProject.addTileForTileTabKey(tilesTab.getKey(), tile.getFile());
              } else if (abstractTab.getTabType() == TabType.BRUSHES && abstractTab instanceof BrushesTabContainer) {
                BrushesTabContainer brushTab = (BrushesTabContainer) abstractTab;
                for (BrushModel brush : brushTab.getBrushModels())
                  currentProject.addTileForTileTabKey(brushTab.getKey(), brush.getFile());
              } else if (abstractTab.getTabType() == TabType.OBJECTS && abstractTab instanceof ObjectsTabContainer) {
                ObjectsTabContainer objectTab = (ObjectsTabContainer) abstractTab;
                for (ObjectModel object : objectTab.getObjectModels())
                  currentProject.addTileForTileTabKey(objectTab.getKey(), object.getFile());
              }
            }
          }
        }
      }
    });

    view.getTabPane().setOnDragOver(event -> {
      System.out.println("ManageTilesController drag over");

      /* data is dragged over the target; accept it only if it is not dragged from the same node and if it has a string data */
      if (event.getGestureSource() != view.getTabPane() && event.getDragboard().hasFiles()) {
        List<File> files = event.getDragboard().getFiles();
        if (files != null && !files.isEmpty() && validateDraggedFiles(files)) {
          event.acceptTransferModes(TransferMode.ANY);
        }
      }
      event.consume();
    });

    view.getTabPane().setOnDragDropped(event -> {
      Dragboard dragboard = event.getDragboard();
      boolean success = false;
      if (event.getGestureSource() != view.getTabPane() && event.getDragboard().hasFiles()) {
        Tab selectedTab = view.getTabPane().getSelectionModel().getSelectedItem();
        if (isValidTab(selectedTab)) {
          AbstractTabContainer abstractTab = (AbstractTabContainer) selectedTab.getUserData();
          if (abstractTab.getTabType() == TabType.TILES && abstractTab instanceof TilesTabContainer) {
            addTileFileToExistingTab((TilesTabContainer) abstractTab, dragboard.getFiles());
            success = true;
          } else if (abstractTab.getTabType() == TabType.BRUSHES && abstractTab instanceof BrushesTabContainer) {
            addBrushFileToExistingTab((BrushesTabContainer) abstractTab, dragboard.getFiles());
            success = true;
          } else if (abstractTab.getTabType() == TabType.OBJECTS && abstractTab instanceof ObjectsTabContainer) {
            addObjectFileToExistingTab((ObjectsTabContainer) abstractTab, dragboard.getFiles());
            success = true;
          }
        }
      }
      event.setDropCompleted(success);
      event.consume();
    });
  }

  private void onAddNewTabSelection() {
    OkCancelDialog window = new OkCancelDialog("Add new tab", null, null, false);
    CreateTilesTabView tilesTabForm = new CreateTilesTabView();
    window.setContent(tilesTabForm.asNode());
    window.getOkButton().setOnAction(event -> {
      window.close();
    });
    window.show();
  }

  private void onTabChanged(Tab oldTab, Tab newTab) {
    if (newTab == null) {
      selectedTileView = null;
      listener.selectedEntityChanged(null);
      return;
    }

    AbstractTabContainer tabContainer = (AbstractTabContainer) newTab.getUserData();
    listener.selectedEntityChanged(tabContainer.getSelectedDrawModel());
    selectedTileView = tabContainer.getSelectedTileView();
  }

  /**
   * Create a new tiles tab and loads the specified images into it.
   * @param title
   * Tab title.
   * @param imageFiles
   * Image files.
   */
  public void addTilesTabForFiles(String title, List<File> imageFiles) {
    if (StringValidator.isNullOrEmpty(title) || imageFiles == null || imageFiles.isEmpty())
      return;

    List<ImageModel> images = loadImageModelListForFiles(imageFiles);
    addTilesTabForModels(title, images);
  }

  private List<ImageModel> loadImageModelListForFiles(List<File> imageFiles) {
    List<ImageModel> images = new ArrayList<>();
    imageFiles.stream().filter(file -> FileExtensionUtil.isImageFile(file.getName())).forEach(file -> {
      ImageModel image = ImageProvider.getImageModel(file);
      if (image != null)
        images.add(image);
    });
    return images;
  }

  /**
   * Add a new brush tab for the specified brush models list.
   * @param title
   * Tab title.
   * @param brushes
   * BrushModel list.
   */
  public void addBrushTabFromXMLModels(String title, List<BrushModel> brushes) {
    if (StringValidator.isNullOrEmpty(title) || brushes == null || brushes.isEmpty())
      return;

    BrushesTabContainer brushesTabContainer = new BrushesTabContainer(title, true);
    for (BrushModel brushModel : brushes)
      brushesTabContainer.addTile(new SelectableBrushView(brushModel, true, this));

    Tab tab = new Tab(title, brushesTabContainer.asNode());
    tab.setUserData(brushesTabContainer);
    view.addTab(tab);
  }

  public void addObjectTabFromXMLModels(String title, List<ObjectModel> objects) {
    if (StringValidator.isNullOrEmpty(title) || objects == null || objects.isEmpty())
      return;

    ObjectsTabContainer objectsTabContainer = new ObjectsTabContainer(title, true);
    for (ObjectModel objectModel : objects)
      objectsTabContainer.addTile(new SelectableObjectView(objectModel, true, this));

    Tab tab = new Tab(title, objectsTabContainer.asNode());
    tab.setUserData(objectsTabContainer);
    view.addTab(tab);
  }

  /**
   * Add a new brush tab for the specified brush files list.
   * @param title
   * Tab title.
   * @param brushFiles
   * Brush files list.
   */
  private void addBrushTabForBrushFiles(String title, List<File> brushFiles) {
    try {
      List<BrushModel> brushes = loadBrushModelListForFiles(brushFiles);
      addBrushTabFromXMLModels(title, brushes);
    } catch (Exception ex) {
      Dialog.showWarningDialog(null, "ManageTilesController error occurred. Error message: " + ex.getMessage());
    }
  }

  private void addObjectTabForObjectFiles(String title, List<File> objectFiles) {
    try {
      List<ObjectModel> objects = loadObjectModelListForFiles(objectFiles);
      addObjectTabFromXMLModels(title, objects);
    } catch (Exception ex) {
      Dialog.showWarningDialog(null, "ManageTilesController error occurred. Error message: " + ex.getMessage());
    }
  }

  private  void addTileFileToExistingTab(TilesTabContainer tilesTabContainer, List<File> tileFiles) {
    if (tilesTabContainer == null || tileFiles == null || tileFiles.isEmpty())
      return;
    try {
      List<ImageModel> imageModels = tilesTabContainer.getTileModels();

      //remove the tiles that are already loaded
      Iterator<File> iterator = tileFiles.iterator();
      while (iterator.hasNext()) {
        File file = iterator.next();
        for (ImageModel imageModel : imageModels) {
          if (file.getAbsolutePath().equalsIgnoreCase(imageModel.getPath()))
            iterator.remove();
        }
      }

      ProjectModel currentProject = AppParameters.CURRENT_PROJECT;

      List<ImageModel> tiles = loadImageModelListForFiles(tileFiles);
      for (ImageModel imageModel : tiles) {
        tilesTabContainer.addTile(new SelectableTileView(imageModel, true, this));
        currentProject.addTileForTileTabKey(tilesTabContainer.getKey(), imageModel.getFile());
      }
    } catch (Exception ex) {
      Dialog.showWarningDialog(null, "ManageTilesController - Unable to add the specified tile files into the current tab. Error message: " + ex.getMessage());
    }
  }

  /**
   * Load and add the specified brush files into the specified brush tab. If a brush is already loaded, it will not be loaded again.
   * @param brushesTabContainer BrushesTabContainer
   * @param brushFiles Brush files.
   */
  private void addBrushFileToExistingTab(BrushesTabContainer brushesTabContainer, List<File> brushFiles) {
    if (brushesTabContainer == null || brushFiles == null || brushFiles.isEmpty())
      return;
    try {
      List<BrushModel> brushModels = brushesTabContainer.getBrushModels();

      // remove the brushes that are already loaded
      Iterator<File> iterator = brushFiles.iterator();
      while (iterator.hasNext()) {
        File file = iterator.next();
        for (BrushModel brushModel : brushModels) {
          if (file.getAbsolutePath().equalsIgnoreCase(brushModel.getPath()))
            iterator.remove();
        }
      }

      ProjectModel currentProject = AppParameters.CURRENT_PROJECT;

      List<BrushModel> brushes = loadBrushModelListForFiles(brushFiles);
      for (BrushModel brushModel : brushes) {
        brushesTabContainer.addTile(new SelectableBrushView(brushModel, true, this));
        currentProject.addTileForTileTabKey(brushesTabContainer.getKey(), brushModel.getFile());
      }
    } catch (Exception ex) {
      Dialog.showWarningDialog(null, "ManageTilesController - Unable to add the specified brush files into the current tab. Error message: " + ex.getMessage());
    }
  }

  private void addObjectFileToExistingTab(ObjectsTabContainer objectsTabContainer, List<File> objectFiles) {
    if (objectsTabContainer == null || objectFiles == null || objectFiles.isEmpty())
      return;
    try {
      List<ObjectModel> objectModels = objectsTabContainer.getObjectModels();

      // remove the objects that are already loaded
      Iterator<File> iterator = objectFiles.iterator();
      while (iterator.hasNext()) {
        File file = iterator.next();
        for (ObjectModel objectModel : objectModels) {
          if (file.getAbsolutePath().equalsIgnoreCase(objectModel.getPath()))
            iterator.remove();
        }
      }

      ProjectModel currentProject = AppParameters.CURRENT_PROJECT;

      List<ObjectModel> objects = loadObjectModelListForFiles(objectFiles);
      for (ObjectModel objectModel : objects) {
        objectsTabContainer.addTile(new SelectableObjectView(objectModel, true, this));
        currentProject.addTileForTileTabKey(objectsTabContainer.getKey(), objectModel.getFile());
      }
    } catch (Exception ex) {
      Dialog.showWarningDialog(null, "ManageTilesController - Unable to add the specified object files into the current tab. Error message: " + ex.getMessage());
    }
  }

  public List<BrushModel> loadBrushModelListForFiles(List<File> brushFiles) throws Exception {
    List<BrushModel> brushModels = MapEditorController.getInstance().getRepoController().openBrushesForFiles(brushFiles, null);
    loadTilesForBrushModels(brushModels);
    return brushModels;
  }

  public List<ObjectModel> loadObjectModelListForFiles(List<File> objectFiles) throws Exception {
    List<ObjectModel> objectModels = MapEditorController.getInstance().getRepoController().openObjectsForFiles(objectFiles, null);
    loadTilesForObjectModels(objectModels);
    return objectModels;
  }

  /**
   * Load all the brush models under the specified directory file.
   * @param dirFile
   * Directory file.
   * @return
   * Brush models list.
   * @throws Exception
   */
  public List<BrushModel> loadBrushModelsListUnderDirectory(File dirFile) throws Exception {
    List<BrushModel> brushes = MapEditorController.getInstance().getRepoController().openBrushesUnderDir(dirFile, null);
    loadTilesForBrushModels(brushes);
    return brushes;
  }

  /**
   * Load all the object models under the specified directory file.
   * @param dirFile
   * Directory file
   * @return
   * Object models list.
   * @throws Exception
   */
  public List<ObjectModel> loadObjectModelsListUnderDirectory(File dirFile) throws Exception {
    List<ObjectModel> objects = MapEditorController.getInstance().getRepoController().openObjectsUnderDir(dirFile, null);
    loadTilesForObjectModels(objects);
    return objects;
  }

  public void addTilesTabForModels(String title, List<ImageModel> tiles) {
    if (StringValidator.isNullOrEmpty(title))
      return;

    TilesTabContainer tilesTabContainer = new TilesTabContainer(title, true);
    for (ImageModel image : tiles)
      tilesTabContainer.addTile(new SelectableTileView(image, true, this));

    Tab tab = new Tab(title, tilesTabContainer.asNode());
    tab.setUserData(tilesTabContainer);
    view.addTab(tab);
  }

  /**
   * Tries to load the tiles composing the brushes.
   * @param brushModels
   * Brush models list.
   * @throws Exception
   */
  private void loadTilesForBrushModels(List<BrushModel> brushModels) throws Exception {
    for (BrushModel brushModel : brushModels) {
      // load primary tiles
      for (BrushTileModel tileModel : brushModel.getPrimaryTiles()) {
        ImageModel imageModel = ImageProvider.getImageModel(tileModel.getPath());
        tileModel.setImageModel(imageModel);
        if (tileModel.getRowIndex() == brushModel.getPrimaryImageY() && tileModel.getColIndex() == brushModel.getPrimaryImageX()) {
          brushModel.setPrimaryImageModel(imageModel);
        }
      }
      // load secondary tiles
      for (BrushTileModel tileModel : brushModel.getSecondaryTiles()) {
        ImageModel imageModel = ImageProvider.getImageModel(tileModel.getPath());
        tileModel.setImageModel(imageModel);
      }
    }
  }

  /**
   * Tries to load the tiles composing the objects.
   * @param objectModels
   * Object models list
   * @throws Exception
   */
  private void loadTilesForObjectModels(List<ObjectModel> objectModels) throws Exception {
    for (ObjectModel objectModel : objectModels) {
      int rows = objectModel.getRows();
      int cols = objectModel.getCols();
      ObjectTileModel[][] objectTileModels = objectModel.getObjectTileModels();
      for (int i = 0; i < rows; i ++) {
        for (int j = 0; j < cols; j ++) {
          ObjectTileModel objectTileModel = objectTileModels[i][j];
          ImageModel imageModel = ImageProvider.getImageModel(objectTileModel.getPath());
          objectTileModel.setImage(imageModel);
        }
      }
    }
  }

  /**
   * Checks to see if the files are valid and they can be dragged over the selected tab.
   * @param files Files list
   * @return true if the files can dragged; false otherwise
   */
  private boolean validateDraggedFiles(List<File> files) {
    Tab selectedItem = view.getTabPane().getSelectionModel().getSelectedItem();
    if (!isValidTab(selectedItem))
      return false;

    AbstractTabContainer abstractTab = (AbstractTabContainer) selectedItem.getUserData();
    if (abstractTab.getTabType() == TabType.TILES && abstractTab instanceof TilesTabContainer) {
      for (File file : files) {
        if (!file.exists() || !FileExtensionUtil.isImageFile(file.getName()))
          return false;
      }
    } else if (abstractTab.getTabType() == TabType.BRUSHES && abstractTab instanceof BrushesTabContainer) {
      for (File file : files) {
        if (!file.exists() || !FileExtensionUtil.isBrushFile(file.getName()))
          return false;
      }
    } else if (abstractTab.getTabType() == TabType.OBJECTS && abstractTab instanceof ObjectsTabContainer) {
      for (File file : files) {
        if (!file.exists() || !FileExtensionUtil.isObjectFile(file.getName()))
          return false;
      }
    } else {
      return false;
    }

    return true;
  }

  private boolean isValidTab(Tab tab) {
    return tab != null && tab.getUserData() != null && tab.getUserData() instanceof AbstractTabContainer;
  }

  @Override
  public void selectedTileChanged(SelectableTileView selectedView) {
    if (selectedTileView == null) {
      selectedTileView = selectedView;
    } else if (selectedTileView != selectedView) {
      selectedTileView.unselect();
      selectedTileView = selectedView;
    }

    listener.selectedEntityChanged(selectedTileView != null ? selectedTileView.getImage() : null);
  }

  @Override
  public void selectedBrushChanged(SelectableBrushView brushView) {
    if (selectedTileView == null) {
      selectedTileView = brushView;
    } else if (selectedTileView != brushView) {
      selectedTileView.unselect();
      selectedTileView = brushView;
    }

    listener.selectedEntityChanged(selectedTileView != null ? brushView.getBrushModel() : null);
  }

  @Override
  public void selectedObjectChanged(SelectableObjectView objectView) {
    if (selectedTileView == null) {
      selectedTileView = objectView;
    } else if (selectedTileView != objectView) {
      selectedTileView.unselect();
      selectedTileView = objectView;
    }

    listener.selectedEntityChanged(selectedTileView != null ? objectView.getObjectModel() : null);
  }

  @Override
  public void selectedCharacterChanged(SelectableCharacterView characterView) {
    // do nothing
  }
}

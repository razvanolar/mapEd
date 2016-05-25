package mapEditor.application.main_part.manage_maps.manage_tiles;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.TabKey;
import mapEditor.application.main_part.app_utils.models.brush.BrushTileModel;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.manage_maps.manage_tiles.create_tiles_tab.CreateTilesTabView;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.AbstractTabContainer;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.BrushesTabContainer;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.TilesTabContainer;
import mapEditor.application.main_part.manage_maps.utils.SelectableBrushView;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectableTileListener;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileView;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectedTileListener;
import mapEditor.application.main_part.manage_maps.utils.TabType;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.app_utils.models.brush.BrushModel;
import mapEditor.application.repo.models.ProjectModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 23.01.2016.
 */
public class ManageTilesController implements Controller, SelectableTileListener {

  public interface IManageTilesView {
    TabPane getTabPane();
    Button getNewTabButton();
    Button getAddTilesButton();
    void addTab(Tab tab);
    Region asNode();
  }

  private IManageTilesView view;
  private SelectableTileView selectedTileView;
  private SelectedTileListener listener;

  public ManageTilesController(IManageTilesView view, SelectedTileListener listener) {
    this.view = view;
    this.listener = listener;
  }

  public void bind() {
    Map<TabKey, List<File>> tabs = AppParameters.CURRENT_PROJECT.getOpenedTileTabs();
    if (tabs != null && !tabs.isEmpty()) {
      for (TabKey key : tabs.keySet()) {
        List<File> drawModels = tabs.get(key);
        if (drawModels != null && drawModels.isEmpty())
          continue;
        if (key.getType() == TabType.TILES)
          addTilesTabForFiles(key.getName(), drawModels);
        else if (key.getType() == TabType.BRUSHES)
          addBrushTabForBrushFiles(key.getName(), drawModels);
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
      while (c.next()) {
        ProjectModel currentProject = AppParameters.CURRENT_PROJECT;

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
              }
            }
          }
        }
      }
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
      listener.selectedTileChanged(null);
      return;
    }

    AbstractTabContainer tabContainer = (AbstractTabContainer) newTab.getUserData();
    listener.selectedTileChanged(tabContainer.getSelectedDrawModel());
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

    List<ImageModel> images = new ArrayList<>();
    imageFiles.stream().filter(file -> FileExtensionUtil.isImageFile(file.getName())).forEach(file -> {
      ImageModel image = ImageProvider.getImageModel(file);
      if (image != null)
        images.add(image);
    });

    addTilesTabForModels(title, images);
  }

  /**
   * Add a new brush tab for the specified brush models list.
   * @param title
   * Tab title.
   * @param brushes
   * BrushModel list.
   */
  public void addBrushTabFromXMLModels(String title, List<BrushModel> brushes) {
    if (StringValidator.isNullOrEmpty(title))
      return;

    BrushesTabContainer tilesTabContainer = new BrushesTabContainer(title, true);
    for (BrushModel brushModel : brushes)
      tilesTabContainer.addTile(new SelectableBrushView(brushModel, true, this));

    Tab tab = new Tab(title, tilesTabContainer.asNode());
    tab.setUserData(tilesTabContainer);
    view.addTab(tab);
  }

  /**
   * Add a new brush tab for the specified brush files list.
   * @param title
   * Tab title.
   * @param brushFiles
   * Brush files list.
   */
  public void addBrushTabForBrushFiles(String title, List<File> brushFiles) {
    try {
      List<BrushModel> brushes = loadBrushModelListForFiles(brushFiles);
      addBrushTabFromXMLModels(title, brushes);
    } catch (Exception ex) {
      Dialog.showWarningDialog(null, "ManageTilesController error occurred. Error message: " + ex.getMessage());
    }
  }

  public List<BrushModel> loadBrushModelListForFiles(List<File> brushFiles) throws Exception {
    List<BrushModel> brushModels = MapEditorController.getInstance().getRepoController().openBrushesForFiles(brushFiles, null);
    loadTilesForBrushModels(brushModels);
    return brushModels;
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

  @Override
  public void selectedTileChanged(SelectableTileView selectedView) {
    if (selectedTileView == null) {
      selectedTileView = selectedView;
    } else if (selectedTileView != selectedView) {
      selectedTileView.unselect();
      selectedTileView = selectedView;
    }

    listener.selectedTileChanged(selectedTileView != null ? selectedTileView.getImage() : null);
  }

  @Override
  public void selectedBrushChanged(SelectableBrushView brushView) {
    if (selectedTileView == null) {
      selectedTileView = brushView;
    } else if (selectedTileView != brushView) {
      selectedTileView.unselect();
      selectedTileView = brushView;
    }

    listener.selectedTileChanged(selectedTileView != null ? brushView.getBrushModel() : null);
  }
}

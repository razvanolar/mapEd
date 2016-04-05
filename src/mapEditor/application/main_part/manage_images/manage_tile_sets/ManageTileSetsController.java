package mapEditor.application.main_part.manage_images.manage_tile_sets;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.FilesLoader;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;
import mapEditor.application.main_part.app_utils.models.MessageType;
import mapEditor.application.main_part.app_utils.models.brush.LWBrushModel;
import mapEditor.application.main_part.app_utils.views.canvas.ImageCanvas;
import mapEditor.application.main_part.app_utils.views.TabImageLoadView;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.characters_player_view.CharactersPlayerController;
import mapEditor.application.main_part.manage_images.manage_tile_sets.characters_player_view.CharactersPlayerView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.configurations.ManageConfigurationController;
import mapEditor.application.main_part.manage_images.manage_tile_sets.create_brush.CreateBrushController;
import mapEditor.application.main_part.manage_images.manage_tile_sets.create_brush.CreateBrushView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.cropped_tiles.detailed_view.CroppedTilesDetailedController;
import mapEditor.application.main_part.manage_images.manage_tile_sets.cropped_tiles.detailed_view.CroppedTileDetailedDetailedView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.cropped_tiles.CroppedTilesPathView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.cropped_tiles.simple_view.CroppedTileSimpleController;
import mapEditor.application.main_part.manage_images.manage_tile_sets.cropped_tiles.simple_view.CroppedTileSimpleView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.ManageImagesListener;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.SaveImageController;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.SaveImageView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.TabContentView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.SystemParameters;
import mapEditor.application.repo.results.SaveImagesResult;
import mapEditor.application.repo.statuses.SaveFilesStatus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ManageTileSetsController implements Controller, ManageImagesListener {

  //TODO: refactor the listeners logic (many of them can be created only once)

  public enum IManageConfigurationViewState {
    /**
     * NO_TAB_SELECTED   - The tab pane of the ManageTileSetsView have no tab registered
     * NO_IMAGE_SELECTED - Tab exists, but no image was selected
     * FULL_SELECTION    - There is an image selected in the current tab, so all effect objects exist
     */
    NO_TAB_SELECTED, NO_IMAGE_SELECTED, FULL_SELECTION
  }

  public interface IManageTileSetsView extends View {
    ScrollPane addTab(String title, TabContentView content);
    boolean isSimpleView();
    TabPane getTabPane();
    Button getAddNewTabButton();
    Button getPlayCharactersButton();
    Button getBrushButton();
    Button getSaveCroppedTilesButton();
    Button getSettingsButton();
    Button getCropSelectionButton();
    Button getSaveTileSetButton();
    Button getResetConfigurationButton();
    ToolBar getTabsToolbar();
    ToolBar getVerticalToolBar();
    ToggleButton getSimpleViewButton();
    ToggleButton getGridSelectionButton();
    ManageConfigurationController.IManageConfigurationView getManageConfigurationView();
    void setState(IManageConfigurationViewState state);
  }

  private IManageTileSetsView view;
  private ManageConfigurationController configurationController;
  private ImageCanvas currentCanvas;
  private TabContentView currentTabContent;
  private Map<TabContentView, CroppedTilesDetailedController> tabDetailedControllerMap;
  private Map<TabContentView, CroppedTileSimpleController> tabSimpleControllerMap;

  private OkCancelDialog saveTilesetDialog;

  public ManageTileSetsController(IManageTileSetsView view) {
    this.view = view;
  }

  public void bind() {
    tabDetailedControllerMap = new HashMap<>();
    tabSimpleControllerMap = new HashMap<>();
    configurationController = new ManageConfigurationController(view.getManageConfigurationView());
    configurationController.bind();
    addListeners();
    addImageTab(SystemParameters.UNTITLED_TAB_NAME, null);
  }

  private void addListeners() {
    view.getAddNewTabButton().setOnAction(event -> onAddButtonSelection());

    view.getSaveCroppedTilesButton().setOnAction(event -> onSaveCroppedTilesSelection());

    view.getTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
      if (newTab != null) {
        currentTabContent = (TabContentView) newTab.getUserData();
        currentCanvas = (ImageCanvas) currentTabContent.getCanvas();
        if (currentCanvas.getImage() != null) {
          configurationController.setViewState(IManageConfigurationViewState.FULL_SELECTION);
          view.setState(IManageConfigurationViewState.FULL_SELECTION);
        } else {
          configurationController.setViewState(IManageConfigurationViewState.NO_IMAGE_SELECTED);
          view.setState(IManageConfigurationViewState.NO_IMAGE_SELECTED);
        }
        configurationController.setListener(currentCanvas);
        currentCanvas.setGridSelection(view.getGridSelectionButton().isSelected());
        currentCanvas.paint();
        currentTabContent.setToolBar(view.getTabsToolbar());
        currentTabContent.setVerticalToolBar(view.getVerticalToolBar());
        view.getSimpleViewButton().setSelected(currentTabContent.isSimpleView());
        canvasWasChanged();
      } else {
        currentCanvas = null;
        currentTabContent = null;
        configurationController.setListener(null);
        configurationController.setViewState(IManageConfigurationViewState.NO_TAB_SELECTED);
        view.setState(IManageConfigurationViewState.NO_TAB_SELECTED);
        view.getSaveTileSetButton().setDisable(true);
        addImageTab(SystemParameters.UNTITLED_TAB_NAME, null);
      }
    });

    view.getTabPane().getTabs().addListener((ListChangeListener<Tab>) c -> {
      while (c.next()) {
        if (c.getRemovedSize() > 0)
          c.getRemoved().stream().filter(tab -> tab.getUserData() != null).forEach(tab -> {
            TabContentView contentView = (TabContentView) tab.getUserData();
            tabDetailedControllerMap.remove(contentView);
            tabSimpleControllerMap.remove(contentView);
          });
      }
    });

    view.getSaveTileSetButton().setOnAction(event -> {
      if (currentCanvas == null || currentCanvas.getUserData() == null || view.getSaveTileSetButton().isDisable())
        return;

      OkCancelDialog dialog = getSaveTilesetDialog();
      ImageModel image = (ImageModel) currentCanvas.getUserData();
      SaveImageController controller = (SaveImageController) dialog.getController();
      controller.setImageModel(image);
      dialog.setShowAdditionalButton(controller.isOverwriteActive());
      dialog.show();
    });

    view.getCropSelectionButton().setOnAction(event -> {
      if (currentCanvas == null || currentTabContent == null)
        return;
      currentCanvas.cropSelection(param -> {
        if (param != null) {
          ImageModel image = new ImageModel(param, AppParameters.CURRENT_PROJECT.getTilesFile().getAbsolutePath(), "");
          addCroppedImage(image);
        }
        return null;
      });
    });

    view.getSimpleViewButton().selectedProperty().addListener((observable, oldValue, newValue) -> {
      onChangeTilesView(newValue);
    });

    view.getGridSelectionButton().selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (currentCanvas != null) {
        currentCanvas.setGridSelection(newValue);
        currentCanvas.paint();
      }
    });

    view.getPlayCharactersButton().setOnAction(event -> {
      if (currentCanvas == null || currentCanvas.getImage() == null)
        return;
      currentCanvas.cropSelectedMatrix(param -> {
        if (param == null || param.getRows() <= 0 || param.getColumns() <= 0)
          return null;

        OkCancelDialog dialog = new OkCancelDialog("Test Characters", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true);
        final CharactersPlayerController.ICharacterPlayerView[] characterPlayerView = {new CharactersPlayerView()};
        final CharactersPlayerController[] charactersPlayerController = {new CharactersPlayerController(characterPlayerView[0],
                param, configurationController.getSquareStrokeColor(), configurationController.getSquareFillColor(),
                currentCanvas.getNumberOfSelectedColumns(), currentCanvas.getNumberOfSelectedRows(),
                currentCanvas.getCellWidth(), currentCanvas.getCellHeight())};
        charactersPlayerController[0].bind();
        dialog.setContent(characterPlayerView[0].asNode());
        dialog.getStage().addEventHandler(WindowEvent.WINDOW_HIDING, event1 -> {
          charactersPlayerController[0].closeAnimation();
          charactersPlayerController[0] = null;
          characterPlayerView[0] = null;
        });
        dialog.show();

        return null;
      });
    });

    view.getBrushButton().setOnAction(event -> onBrushSelection());
  }

  private void onBrushSelection() {
    if (currentCanvas == null || currentCanvas.getImage() == null)
      return;

    OkCancelDialog dialog = new OkCancelDialog("Create Brush", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true, 800, 600);

    CreateBrushController.ICreateBrushView brushView = new CreateBrushView();
    CreateBrushController brushController = new CreateBrushController(brushView, currentCanvas.getUpdatedImage(), dialog.getOkButton(), dialog.getStage());
    brushController.bind();

    dialog.getOkButton().setOnAction(event -> {
      if (!brushController.isValidSelection()) {
        Dialog.showWarningDialog(null, "Fields are not correctly completed!", dialog.getStage());
        return;
      }
      String path = brushController.getSelectedPath();
      List<LWBrushModel> brushModels = brushController.getBrushModels();
      try {
        SaveFilesStatus status = MapEditorController.getInstance().getRepoController().saveBrushModels(brushModels, path);
        if (status != SaveFilesStatus.COMPLETE)
          Dialog.showWarningDialog(null, status.getMessage(), dialog.getStage());
        else
          Dialog.showInformDialog(null, "All brushes were saved successfully", dialog.getStage());
      } catch (Exception ex) {
        ex.printStackTrace();
        Dialog.showErrorDialog("Error", "ManageTileSetsController - An error occurred while saving the brushes");
      }
      dialog.close();
    });

    dialog.setContent(brushView.asNode());
    dialog.show();
  }

  private void onChangeTilesView(boolean newValue) {
    if (newValue) {
      CroppedTilesDetailedController detailedController = tabDetailedControllerMap.get(currentTabContent);
      if (detailedController != null) {
        List<ImageModel> images = detailedController.getImages();
        tabSimpleControllerMap.remove(currentTabContent);
        CroppedTileSimpleController.ICroppedTileSimpleView simpleView = new CroppedTileSimpleView();
        CroppedTileSimpleController simpleController = new CroppedTileSimpleController(simpleView);
        simpleController.bind();
        simpleController.addImages(images);
        tabSimpleControllerMap.put(currentTabContent, simpleController);
        tabDetailedControllerMap.remove(currentTabContent);
        if (!images.isEmpty())
          currentTabContent.setSimpleTileView(simpleView.asNode());
      }
    } else {
      CroppedTileSimpleController simpleController = tabSimpleControllerMap.get(currentTabContent);
      if (simpleController != null) {
        List<ImageModel> images = simpleController.getImages();
        tabDetailedControllerMap.remove(currentTabContent);
        CroppedTilesDetailedController detailedController = new CroppedTilesDetailedController(
                AppParameters.CURRENT_PROJECT.getTilesFile(), this);
        detailedController.bind();
        currentTabContent.clearTilesPane();
        for (ImageModel image : images) {
          CroppedTilesDetailedController.ICroppedTileDetailedView view = new CroppedTileDetailedDetailedView(image);
          detailedController.addView(view);
          currentTabContent.addDetailedTileForm(view.asNode());
        }
        tabDetailedControllerMap.put(currentTabContent, detailedController);
        tabSimpleControllerMap.remove(currentTabContent);
      }
    }
    currentTabContent.setSimpleView(newValue);
  }

  private void addCroppedImage(ImageModel image) {
    if (!view.isSimpleView()) {
      CroppedTilesDetailedController controller = tabDetailedControllerMap.get(currentTabContent);
      if (controller == null) {
        controller = new CroppedTilesDetailedController(AppParameters.CURRENT_PROJECT.getTilesFile(), ManageTileSetsController.this);
        controller.bind();
        tabDetailedControllerMap.put(currentTabContent, controller);
      }
      CroppedTilesDetailedController.ICroppedTileDetailedView croppedTileView = new CroppedTileDetailedDetailedView(image);
      controller.addView(croppedTileView);
      currentTabContent.addDetailedTileForm(croppedTileView.asNode());
    } else {
      CroppedTileSimpleController controller = tabSimpleControllerMap.get(currentTabContent);
      if (controller == null) {
        CroppedTileSimpleController.ICroppedTileSimpleView view = new CroppedTileSimpleView();
        controller = new CroppedTileSimpleController(view);
        controller.bind();
        currentTabContent.setSimpleTileView(view.asNode());
        tabSimpleControllerMap.put(currentTabContent, controller);
      }
      controller.addImage(image);
      if (currentTabContent.isEmptyTilesPane() && !controller.getImages().isEmpty()) {
        currentTabContent.setSimpleTileView(controller.getView().asNode());
      }
    }
  }

  /**
   * Called when user want to create a new image tab
   */
  private void onAddButtonSelection() {
    OkCancelDialog dialog = new OkCancelDialog("", null, null, false);
    TabImageLoadView loadView = new TabImageLoadView(dialog.getStage());

    // TODO: add a warning label in the loadView UI in order to inform user why the inputs are incorrect
    dialog.getOkButton().setOnAction(event -> {
      String tabName = loadView.getTabTitle();
      if (!StringValidator.isNullOrEmpty(tabName) && !checkIfTabNameExists(tabName)) {
        addImageTab(tabName, loadView.getImageModel());
        dialog.close();
      }
    });

    dialog.setContent(loadView.asNode());
    dialog.show();
  }

  private void onSaveCroppedTilesSelection() {
    if (currentTabContent == null)
      return;
    String selectedPath = currentTabContent.getPathView().getSelectedPath();
    boolean usePathForAllTiles = StringValidator.isValidTilesPath(selectedPath) && currentTabContent.getPathView().usePathForAllTiles();
    if (view.isSimpleView()) {
      CroppedTileSimpleController controller = tabSimpleControllerMap.get(currentTabContent);
      if (controller == null)
        return;
      saveCroppedTiles(controller.getImages(), usePathForAllTiles ? selectedPath : null, usePathForAllTiles);
    } else {
      CroppedTilesDetailedController controller = tabDetailedControllerMap.get(currentTabContent);
      if (controller == null)
        return;
      saveCroppedTiles(controller.getImages(), usePathForAllTiles ? selectedPath : null, usePathForAllTiles);
    }
  }

  private void saveCroppedTiles(List<ImageModel> images, String selectedPath, boolean usePathForAllTiles) {
    if (images == null || images.isEmpty())
      return;
    boolean areImagesWithNoName = false;
    boolean areImagesWithNoPath = false;
    /*
      check and inform the user if there are images without a name or path set
     */
    for (ImageModel image : images) {
      if (StringValidator.isNullOrEmpty(image.getName()))
        areImagesWithNoName = true;
      if (usePathForAllTiles)
        image.setPath(selectedPath);
      else if (!StringValidator.isValidTilesPath(image.getPath()))
        areImagesWithNoPath = true;
    }

    if (areImagesWithNoName) {
      boolean result = Dialog.showConfirmDialog(null, "There are images without name set. Do you want to add automatically a name for them ?");
      if (!result)
        return;
    }

    if (areImagesWithNoPath) {
      boolean result = Dialog.showConfirmDialog(null, "There are images without path set. Do you want to use the default path for them ?");
      if (!result)
        return;
    }

    /*
      if there are such images and the user wants to continue, automatically set the values
     */
    if (areImagesWithNoName || areImagesWithNoPath) {
      String defaultPath = AppParameters.CURRENT_PROJECT.getTilesFile().getAbsolutePath();
      for (ImageModel image : images) {
        if (StringValidator.isNullOrEmpty(image.getName()))
          image.setName(AppParameters.CURRENT_PROJECT.nextHexValue() + KnownFileExtensions.PNG.getExtension());
        if (!StringValidator.isValidTilesPath(image.getPath()))
          image.setPath(defaultPath);
      }
    }

    MapEditorController.getInstance().maskView();
    SaveImagesResult result = MapEditorController.getInstance().getRepoController().saveImages(images);
    SaveFilesStatus status = result.getStatus();

    if (status == SaveFilesStatus.COMPLETE) {
      clearCroppedTiles();
    } else if (status == SaveFilesStatus.PARTIAL) {
      clearCroppedTiles();
      List<ImageModel> unsavedImages = result.getUnsavedImages();
      if (unsavedImages != null) {
        for (ImageModel image : unsavedImages)
          addCroppedImage(image);
      }
    }

    MapEditorController.getInstance().unmaskView();

    Dialog.showInformDialog(null, status.getMessage());
  }

  private void addCanvasListeners(ImageCanvas canvas) {
    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
      if (event.getButton().equals(MouseButton.PRIMARY) && canvas.getImage() == null) {
        loadCanvasImage(canvas);
      }
    });

    canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2 && canvas.getImage() != null) {
        loadCanvasImage(canvas);
      }
    });
  }

  private void loadCanvasImage(ImageCanvas canvas) {
    FilesLoader.getInstance().loadImageModel(param -> {
      if (param == null)
        return null;
      canvas.setImage(param.getImage());
      canvas.setUserData(param);
      canvas.paint();
      configurationController.setViewState(IManageConfigurationViewState.FULL_SELECTION);
      configurationController.setListener(canvas);
      canvasWasChanged();
      return null;
    }, null);
  }

  private void addImageTab(String title, ImageModel image) {
    ImageCanvas canvas = new ImageCanvas(image != null ? image.getImage() : null);
    canvas.setUserData(image);

    TabContentView content = new TabContentView(canvas, createPathView());
    ScrollPane pane = view.addTab(title, content);

    ChangeListener<Number> changeListener = (observable, oldValue, newValue) -> canvas.paint();
    canvas.widthProperty().bind(pane.widthProperty());
    canvas.heightProperty().bind(pane.heightProperty());
    pane.widthProperty().addListener(changeListener);
    pane.heightProperty().addListener(changeListener);
    addCanvasListeners(canvas);

//    addTabContentListener(content);
  }

  private CroppedTilesPathView createPathView() {
    CroppedTilesPathView pathView = new CroppedTilesPathView();

    pathView.getPathTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null || !newValue.contains(AppParameters.CURRENT_PROJECT.getTilesFile().getAbsolutePath())) {
        pathView.getUseForAllCheckBox().setSelected(false);
        pathView.getUseForAllCheckBox().setDisable(true);
      } else
        pathView.getUseForAllCheckBox().setDisable(false);
    });

    pathView.getPathButton().setOnAction(event -> {
      OkCancelDialog dialog = new OkCancelDialog("Select Path", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true);
      SystemFilesView filesView = new SystemFilesView(dialog.getOkButton(), AppParameters.CURRENT_PROJECT.getTilesFile(), true, null);
      dialog.setContent(filesView.asNode());

      dialog.getOkButton().setOnAction(event1 -> {
        pathView.getPathTextField().setText(filesView.getSelectedPath());
        dialog.close();
      });

      dialog.show();
    });

    pathView.getPathTextField().setText(AppParameters.CURRENT_PROJECT.getTilesFile().getAbsolutePath());

    return pathView;
  }

  private boolean checkIfTabNameExists(String tabName) {
    for (Tab tab : view.getTabPane().getTabs())
      if (tab.getText().equals(tabName))
        return true;
    return false;
  }

  private void canvasWasChanged() {
    if (currentCanvas == null || currentCanvas.getUserData() == null) {
      view.getSaveTileSetButton().setDisable(true);
      view.setState(IManageConfigurationViewState.NO_IMAGE_SELECTED);
      return;
    }

//    ImageModel image = (ImageModel) currentCanvas.getUserData();
//    String imagePath = image.getPath();
//    String tileSetsPath = AppParameters.CURRENT_PROJECT.getTileSetsFile().getAbsolutePath();
//    view.getSaveTileSetButton().setDisable(imagePath.contains(tileSetsPath));
    view.getSaveTileSetButton().setDisable(false);
    view.setState(IManageConfigurationViewState.FULL_SELECTION);
  }

  private void clearCroppedTiles() {
    if (currentTabContent == null)
      return;
    currentTabContent.clearTilesPane();
    CroppedTilesDetailedController detailedController = tabDetailedControllerMap.get(currentTabContent);
    CroppedTileSimpleController simpleController = tabSimpleControllerMap.get(currentTabContent);
    if (detailedController != null)
      detailedController.deleteAllImages();
    if (simpleController != null)
      simpleController.deleteAllImages();
  }

  public void addNewTab(String title, ImageModel image) {
    if (!checkIfTabNameExists(title))
      addImageTab(title, image);
    else
      Dialog.showWarningDialog(null, "A tab named '" + title + "' already exists.");
  }

  public View getView() {
    return view;
  }

  @Override
  public void saveCroppedImage(CroppedTilesDetailedController.ICroppedTileDetailedView view) {
    if (currentTabContent == null)
      return;
  }

  @Override
  public void dropCroppedTileView(CroppedTilesDetailedController.ICroppedTileDetailedView view) {
    if (currentTabContent == null)
      return;
    int remainedItems = currentTabContent.removeDetailedTileForm(view.asNode());
    if (remainedItems == 0) {
      tabDetailedControllerMap.remove(currentTabContent);
    }
  }

  private OkCancelDialog getSaveTilesetDialog() {
    if (saveTilesetDialog == null) {
      saveTilesetDialog = new OkCancelDialog("Save Image", StageStyle.UTILITY, Modality.APPLICATION_MODAL, false);

      SaveImageController.ISaveImageView saveImageView = new SaveImageView();
      SaveImageController controller = new SaveImageController(saveImageView,
              KnownFileExtensions.PNG,
              AppParameters.CURRENT_PROJECT.getTileSetsFile(),
              saveTilesetDialog.getOkButton());
      controller.bind();

      saveTilesetDialog.setController(controller);
      saveTilesetDialog.getOkButton().setOnAction(event1 -> currentCanvas.cropFullImage(param -> {
        ImageModel image = controller.getImageModel();
        if (image != null) {
          synchronized (SystemParameters.MESSAGE_KEY) {
            SystemParameters.MESSAGE_KEY.setName(saveImageView.getNameTextField().getText());
            SystemParameters.MESSAGE_KEY.setPath(saveImageView.getPathTextField().getText());
            SystemParameters.MESSAGE_KEY.setImageModel(new ImageModel(param, image.getPath(), image.getName()));
            SystemParameters.MESSAGE_KEY.setMessageType(MessageType.SAVE_TILE_SET_IMAGE);
            SystemParameters.MESSAGE_KEY.notify();
            saveTilesetDialog.close();
          }
        }
        return null;
      }));

      Button overwriteButton = new Button("Overwrite");
      overwriteButton.setOnAction(event -> overwriteTileset(controller));

      saveTilesetDialog.setAdditionButton(overwriteButton);
      saveTilesetDialog.setContent(saveImageView.asNode());
    }
    return saveTilesetDialog;
  }

  private void overwriteTileset(SaveImageController controller) {
    currentCanvas.cropFullImage(param -> {
      ImageModel image = controller.getImageModel();
      if (image != null && controller.isValidSelection(image.getName(), image.getPath())) {
        synchronized (SystemParameters.MESSAGE_KEY) {
          SystemParameters.MESSAGE_KEY.setName(image.getName());
          SystemParameters.MESSAGE_KEY.setPath(image.getPath());
          SystemParameters.MESSAGE_KEY.setImageModel(new ImageModel(param, new File(image.getPath())));
          SystemParameters.MESSAGE_KEY.setMessageType(MessageType.OVERWRITE_TILE_SET_IMAGE);
          SystemParameters.MESSAGE_KEY.notify();
          saveTilesetDialog.close();
        }
      }
      return null;
    });
  }
}

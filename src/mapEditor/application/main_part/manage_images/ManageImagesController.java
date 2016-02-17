package mapEditor.application.main_part.manage_images;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.ImagesLoader;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.MessageType;
import mapEditor.application.main_part.app_utils.views.canvas.ImageCanvas;
import mapEditor.application.main_part.app_utils.views.TabImageLoadView;
import mapEditor.application.main_part.app_utils.views.dialogs.AlertDialog;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.manage_images.configurations.ManageConfigurationController;
import mapEditor.application.main_part.manage_images.cropped_tiles.detailed_view.CroppedTilesDetailedController;
import mapEditor.application.main_part.manage_images.cropped_tiles.detailed_view.CroppedTileDetailedDetailedView;
import mapEditor.application.main_part.manage_images.cropped_tiles.CroppedTilesPathView;
import mapEditor.application.main_part.manage_images.cropped_tiles.simple_view.CroppedTileSimpleController;
import mapEditor.application.main_part.manage_images.cropped_tiles.simple_view.CroppedTileSimpleView;
import mapEditor.application.main_part.manage_images.utils.ManageImagesListener;
import mapEditor.application.main_part.manage_images.utils.SaveImageController;
import mapEditor.application.main_part.manage_images.utils.SaveImageView;
import mapEditor.application.main_part.manage_images.utils.TabContentView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.SystemParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ManageImagesController implements Controller, ManageImagesListener {

  //TODO: refactor the listeners logic (many of them can be created only once)

  public enum IManageConfigurationViewState {
    /**
     * NO_TAB_SELECTED   - The tab pane of the ManageImagesView have no tab registered
     * NO_IMAGE_SELECTED - Tab exists, but no image was selected
     * FULL_SELECTION    - There is an image selected in the current tab, so all effect objects exist
     */
    NO_TAB_SELECTED, NO_IMAGE_SELECTED, FULL_SELECTION
  }

  public interface IManageImagesView extends View {
    ScrollPane addTab(String title, TabContentView content);
    TabPane getTabPane();
    Button getAddNewTabButton();
    Button getRemoveTabButton();
    Button getRenameTabButton();
    Button getSettingsButton();
    Button getCropSelectionButton();
    Button getSaveTileSetButton();
    Button getResetConfigurationButton();
    ToolBar getTabsToolbar();
    ManageConfigurationController.IManageConfigurationView getManageConfigurationView();
    void setState(IManageConfigurationViewState state);
  }

  private IManageImagesView view;
  private ManageConfigurationController configurationController;
  private ImageCanvas currentCanvas;
  private TabContentView currentTabContent;
  private Map<TabContentView, CroppedTilesDetailedController> tabDetailedControllerMap;
  private Map<TabContentView, CroppedTileSimpleController> tabSimpleControllerMap;

  public ManageImagesController(IManageImagesView view) {
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
        currentCanvas.paint();
        currentTabContent.setToolBar(view.getTabsToolbar());
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

      ImageModel image = (ImageModel) currentCanvas.getUserData();

      OkCancelDialog dialog = new OkCancelDialog("Save Image", StageStyle.UTILITY, Modality.APPLICATION_MODAL, false);

      SaveImageController.ISaveImageView saveImageView = new SaveImageView();
      SaveImageController controller = new SaveImageController(saveImageView,
              FileExtensionUtil.getFileExtension(image.getImageName()),
              AppParameters.CURRENT_PROJECT.getTileSetsFile(),
              dialog.getOkButton());
      controller.bind();

      dialog.getOkButton().setOnAction(event1 -> {
        synchronized (SystemParameters.MESSAGE_KEY) {
          SystemParameters.MESSAGE_KEY.setName(saveImageView.getNameTextField().getText());
          SystemParameters.MESSAGE_KEY.setPath(saveImageView.getPathTextField().getText());
          SystemParameters.MESSAGE_KEY.setImagePath(image.getImagePath());
          SystemParameters.MESSAGE_KEY.setImageModel(image);
          SystemParameters.MESSAGE_KEY.setButton(view.getSaveTileSetButton());
          SystemParameters.MESSAGE_KEY.setMessageType(MessageType.SAVE_TILE_SET_IMAGE);
          SystemParameters.MESSAGE_KEY.notify();
          dialog.close();
        }
      });

      dialog.setContent(saveImageView.asNode());
      dialog.show();
    });

    view.getCropSelectionButton().setOnAction(event -> {
      if (currentCanvas == null || currentTabContent == null)
        return;
      currentCanvas.cropSelectedTile(param -> {
        if (param != null) {
          ImageModel image = new ImageModel(param, AppParameters.CURRENT_PROJECT.getTilesFile().getAbsolutePath(), "");
          if (!currentTabContent.isSimpleView()) {
            CroppedTilesDetailedController controller = tabDetailedControllerMap.get(currentTabContent);
            if (controller == null) {
              controller = new CroppedTilesDetailedController(AppParameters.CURRENT_PROJECT.getTilesFile(), ManageImagesController.this);
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
          }
        }
        return null;
      });
    });
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
    ImagesLoader.getInstance().loadImageModel(param -> {
      if (param == null)
        return null;
      canvas.setImage(param.getImage());
      canvas.setUserData(param);
      canvas.paint();
      configurationController.setListener(canvas);
      configurationController.setViewState(IManageConfigurationViewState.FULL_SELECTION);
      canvasWasChanged();
      return null;
    }, null);
  }

  private void addTabContentListener(TabContentView content) {
    content.getSimpleViewButton().selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        CroppedTilesDetailedController detailedController = tabDetailedControllerMap.get(currentTabContent);
        if (detailedController != null) {
          List<ImageModel> images = detailedController.getImages();
          tabSimpleControllerMap.remove(currentTabContent);
          CroppedTileSimpleController.ICroppedTileSimpleView simpleView = new CroppedTileSimpleView();
          CroppedTileSimpleController simpleController = new CroppedTileSimpleController(simpleView);
          simpleController.addImages(images);
          tabSimpleControllerMap.put(currentTabContent, simpleController);
          tabDetailedControllerMap.remove(currentTabContent);
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
    });
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

    addTabContentListener(content);
  }

  private CroppedTilesPathView createPathView() {
    CroppedTilesPathView pathView = new CroppedTilesPathView();

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

    ImageModel image = (ImageModel) currentCanvas.getUserData();
    String imagePath = image.getImagePath();
    String tileSetsPath = AppParameters.CURRENT_PROJECT.getTileSetsFile().getAbsolutePath();
    view.getSaveTileSetButton().setDisable(imagePath.contains(tileSetsPath));
    view.setState(IManageConfigurationViewState.FULL_SELECTION);
  }

  public void addNewTab(String title, ImageModel image) {
    if (!checkIfTabNameExists(title))
      addImageTab(title, image);
    else
      AlertDialog.showDialog(null, "A tab named '" + title + "' already exists.");
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
}

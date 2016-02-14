package mapEditor.application.main_part.manage_images;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.ImagesLoader;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageLoaderModel;
import mapEditor.application.main_part.app_utils.models.MessageType;
import mapEditor.application.main_part.app_utils.views.canvas.ImageCanvas;
import mapEditor.application.main_part.app_utils.views.TabImageLoadView;
import mapEditor.application.main_part.app_utils.views.dialogs.AlertDialog;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.manage_images.configurations.ManageConfigurationController;
import mapEditor.application.main_part.manage_images.cropped_tiles.CroppedTileController;
import mapEditor.application.main_part.manage_images.cropped_tiles.CroppedTileView;
import mapEditor.application.main_part.manage_images.cropped_tiles.CroppedTilesPathView;
import mapEditor.application.main_part.manage_images.utils.ManageImagesListener;
import mapEditor.application.main_part.manage_images.utils.SaveImageController;
import mapEditor.application.main_part.manage_images.utils.SaveImageView;
import mapEditor.application.main_part.manage_images.utils.TabContentView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.SystemParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ManageImagesController implements Controller, ManageImagesListener {

  public enum IManageConfigurationViewState {
    /**
     * NO_TAB_SELECTED   - The tab pane of the ManageImagesView have no tab registered
     * NO_IMAGE_SELECTED - Tab exists, but no image was selected
     * FULL_SELECTION    - There is an image selected in the current tab, so all effect objects exist
     */
    NO_TAB_SELECTED, NO_IMAGE_SELECTED, FULL_SELECTION
  }

  public interface IManageImagesView extends View {
    ScrollPane addTab(String title, Canvas canvas, CroppedTilesPathView pathView);
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
  private Map<TabContentView, List<CroppedTileController>> tabControllersMap;

  public ManageImagesController(IManageImagesView view) {
    this.view = view;
  }

  public void bind() {
    tabControllersMap = new HashMap<>();
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
            tabControllersMap.remove(contentView);
          });
      }
    });

    view.getSaveTileSetButton().setOnAction(event -> {
      if (currentCanvas == null || currentCanvas.getUserData() == null || view.getSaveTileSetButton().isDisable())
        return;

      ImageLoaderModel image = (ImageLoaderModel) currentCanvas.getUserData();

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
          SystemParameters.MESSAGE_KEY.setImageLoaderModel(image);
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
          CroppedTileController.ICroppedTileView croppedTileView = new CroppedTileView(param);
          CroppedTileController croppedTileController = new CroppedTileController(croppedTileView,
                  AppParameters.CURRENT_PROJECT.getTilesFile(),
                  ManageImagesController.this);
          croppedTileController.bind();
          currentTabContent.addTileForm(croppedTileView.asNode());
          List<CroppedTileController> values = tabControllersMap.get(currentTabContent);
          if (values == null) {
            values = new ArrayList<>();
            values.add(croppedTileController);
            tabControllersMap.put(currentTabContent, values);
          } else
            values.add(croppedTileController);
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

  private void addImageTab(String title, ImageLoaderModel image) {
    ImageCanvas canvas = new ImageCanvas(image != null ? image.getImage() : null);
    canvas.setUserData(image);

    ScrollPane pane = view.addTab(title, canvas, createPathView());

    ChangeListener<Number> changeListener = (observable, oldValue, newValue) -> {
        canvas.paint();
    };
    canvas.widthProperty().bind(pane.widthProperty());
    canvas.heightProperty().bind(pane.heightProperty());
    pane.widthProperty().addListener(changeListener);
    pane.heightProperty().addListener(changeListener);
    addCanvasListeners(canvas);
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

    ImageLoaderModel image = (ImageLoaderModel) currentCanvas.getUserData();
    String imagePath = image.getImagePath();
    String tileSetsPath = AppParameters.CURRENT_PROJECT.getTileSetsFile().getAbsolutePath();
    view.getSaveTileSetButton().setDisable(imagePath.contains(tileSetsPath));
    view.setState(IManageConfigurationViewState.FULL_SELECTION);
  }

  public void addNewTab(String title, ImageLoaderModel image) {
    if (!checkIfTabNameExists(title))
      addImageTab(title, image);
    else
      AlertDialog.showDialog(null, "A tab named '" + title + "' already exists.");
  }

  public View getView() {
    return view;
  }

  @Override
  public void saveCroppedImage(CroppedTileController.ICroppedTileView view) {
    if (currentTabContent == null)
      return;
  }

  @Override
  public void dropCroppedTileView(CroppedTileController.ICroppedTileView view) {
    if (currentTabContent == null)
      return;
    int remainedItems = currentTabContent.removeTileForm(view.asNode());
    if (remainedItems == 0)
      tabControllersMap.remove(currentTabContent);
  }
}

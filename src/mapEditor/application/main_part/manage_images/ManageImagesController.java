package mapEditor.application.main_part.manage_images;

import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import mapEditor.application.main_part.app_utils.inputs.ImagesLoader;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.views.canvas.ImageCanvas;
import mapEditor.application.main_part.app_utils.views.TabImageLoadView;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.manage_images.configurations.ManageConfigurationController;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ManageImagesController implements Controller {

  public enum IManageConfigurationViewState {
    /**
     * NO_TAB_SELECTED   - The tab pane of the ManageImagesView have no tab registered
     * NO_IMAGE_SELECTED - Tab exists, but no image was selected
     * FULL_SELECTION    - There is an image selected in the current tab, so all effect objects exist
     */
    NO_TAB_SELECTED, NO_IMAGE_SELECTED, FULL_SELECTION
  }

  public interface IManageImagesView extends View {
    ScrollPane addTab(String title, Canvas canvas);
    TabPane getTabPane();
    Button getAddNewTabButton();
    Button getRemoveTabButton();
    Button getRenameTabButton();
    Button getSettingsButton();
    Button getResetConfigurationButton();
    ManageConfigurationController.IManageConfigurationView getManageConfigurationView();
  }

  private IManageImagesView view;
  private ManageConfigurationController configurationController;
  private ImageCanvas currentCanvas;

  public ManageImagesController(IManageImagesView view) {
    this.view = view;
  }

  public void bind() {
    configurationController = new ManageConfigurationController(view.getManageConfigurationView());
    configurationController.bind();
    addListeners();
    addImageTab("Test1", null);
  }

  private void addListeners() {
    view.getAddNewTabButton().setOnAction(event -> onAddButtonSelection());

    view.getTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
      if (newTab != null) {
        currentCanvas = (ImageCanvas) newTab.getUserData();
        if (currentCanvas.getImage() != null) {
          configurationController.setViewState(IManageConfigurationViewState.FULL_SELECTION);
        } else {
          configurationController.setViewState(IManageConfigurationViewState.NO_IMAGE_SELECTED);
        }
        configurationController.setListener(currentCanvas);
        currentCanvas.paint();
        newTab.setContent(newTab.getContent());
      } else {
        configurationController.setListener(null);
        configurationController.setViewState(IManageConfigurationViewState.NO_TAB_SELECTED);
      }
    });
  }

  /**
   * Called when user want to create a new image tab
   */
  private void onAddButtonSelection() {
    OkCancelDialog dialog = new OkCancelDialog("");
    TabImageLoadView loadView = new TabImageLoadView(dialog.getStage());

    // TODO: add a warning label in the loadView UI in order to inform user why the inputs are incorrect
    dialog.getOkButton().setOnAction(event -> {
      String tabName = loadView.getTabTitle();
      if (!StringValidator.isNullOrEmpty(tabName) && !checkIfTabNameExists(tabName)) {
        addImageTab(tabName, loadView.getImage());
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
    ImagesLoader.getInstance().loadImage(param -> {
      canvas.setImage(param);
      canvas.paint();
      if (param != null) {
        configurationController.setListener(canvas);
        configurationController.setViewState(IManageConfigurationViewState.FULL_SELECTION);
      }
      return null;
    }, null);
  }

  private void addImageTab(String title, Image image) {
    ImageCanvas canvas = new ImageCanvas(image);

    ScrollPane pane = view.addTab(title, canvas);

    ChangeListener<Number> changeListener = (observable, oldValue, newValue) -> {
      if (canvas == currentCanvas)
        canvas.paint();
    };
    canvas.widthProperty().bind(pane.widthProperty());
    canvas.heightProperty().bind(pane.heightProperty());
    pane.widthProperty().addListener(changeListener);
    pane.heightProperty().addListener(changeListener);
    addCanvasListeners(canvas);
  }

  private boolean checkIfTabNameExists(String tabName) {
    for (Tab tab : view.getTabPane().getTabs())
      if (tab.getText().equals(tabName))
        return true;
    return false;
  }

  public View getView() {
    return view;
  }
}

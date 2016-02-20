package mapEditor.application.main_part.manage_images.cropped_tiles.simple_view.context_menu;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.manage_images.utils.SaveImageController;
import mapEditor.application.main_part.manage_images.utils.SaveImageView;
import mapEditor.application.main_part.types.Controller;

/**
 *
 * Created by razvanolar on 17.02.2016.
 */
public class SimpleCroppedTileContextMenuController implements Controller {

  private enum CroppedTileEditState {
    NAME, PATH, NONE
  }

  public interface ISimpleCroppedTileContextMenuView {
    ContextMenu getContextMenu();
    MenuItem getSetNameMenuItem();
    MenuItem getSetPathMenuItem();
    MenuItem getSaveMenuItem();
    MenuItem getDropMenuItem();
  }

  private ISimpleCroppedTileContextMenuView view;
  private SaveImageController saveImageController;
  private OkCancelDialog dialog;

  private ImageModel currentImageModel;
  private CroppedTileEditState editState;

  public SimpleCroppedTileContextMenuController(ISimpleCroppedTileContextMenuView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    addListeners();
  }

  private void addListeners() {
    view.getSetNameMenuItem().setOnAction(event -> {
      SaveImageController.ISaveImageView view = getSaveImageController().getView();
      view.setState(SaveImageController.ISaveImageViewState.NAME);
      getSaveImageController().setName(currentImageModel.getImageName());
      getSaveImageController().validateInput();
      OkCancelDialog okDialog = getDialog();
      okDialog.setContent(view.asNode());
      editState = CroppedTileEditState.NAME;
      okDialog.show();
    });

    view.getSetPathMenuItem().setOnAction(event -> {
      SaveImageController.ISaveImageView view = getSaveImageController().getView();
      view.setState(SaveImageController.ISaveImageViewState.PATH);
      getSaveImageController().setPath(currentImageModel.getImagePath());
      getSaveImageController().validateInput();
      OkCancelDialog okDialog = getDialog();
      okDialog.setContent(view.asNode());
      editState = CroppedTileEditState.PATH;
      okDialog.show();
    });
  }

  public void showContextMenu(Node node) {
    view.getContextMenu().hide();
    view.getContextMenu().show(node, Side.RIGHT, -5, 5);
  }

  private SaveImageController getSaveImageController() {
    if (saveImageController == null) {
      saveImageController = new SaveImageController(new SaveImageView(), KnownFileExtensions.PNG,
              AppParameters.CURRENT_PROJECT.getTilesFile(), getDialog().getOkButton());
      saveImageController.bind();
    }
    return saveImageController;
  }

  private OkCancelDialog getDialog() {
    if (dialog == null) {
      dialog = new OkCancelDialog("Set Attribute", StageStyle.UTILITY, Modality.APPLICATION_MODAL, false);
      dialog.getOkButton().setOnAction(event -> {
        String name = getSaveImageController().getName();
        String path = getSaveImageController().getPath();
        if (editState == CroppedTileEditState.NAME && !StringValidator.isNullOrEmpty(name)) {
          currentImageModel.setImageName(name);
        } else if (editState == CroppedTileEditState.PATH && !StringValidator.isNullOrEmpty(path)) {
          currentImageModel.setImagePath(path);
        }
        dialog.close();
        editState = CroppedTileEditState.NONE;
      });
    }
    return dialog;
  }

  public void setCurrentImageModel(ImageModel currentImageModel) {
    this.currentImageModel = currentImageModel;
  }
}

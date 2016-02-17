package mapEditor.application.main_part.manage_images.cropped_tiles.simple_view.context_menu;

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.AppParameters;
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
      getSaveImageController().validateInput();
      OkCancelDialog okDialog = getDialog();
      okDialog.setContent(view.asNode());

      okDialog.show();
    });

    view.getSetPathMenuItem().setOnAction(event -> {
      SaveImageController.ISaveImageView view = getSaveImageController().getView();
      view.setState(SaveImageController.ISaveImageViewState.PATH);
      getSaveImageController().validateInput();
      OkCancelDialog okDialog = getDialog();
      okDialog.setContent(view.asNode());

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
    }
    return dialog;
  }
}

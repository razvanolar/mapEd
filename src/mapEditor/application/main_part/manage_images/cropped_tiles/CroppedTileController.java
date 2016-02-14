package mapEditor.application.main_part.manage_images.cropped_tiles;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.manage_images.utils.ManageImagesListener;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.io.File;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public class CroppedTileController implements Controller {

  public interface ICroppedTileView extends View {
    TextField getNameTextField();
    TextField getPathTextField();
    Button getPathButton();
    Button getSaveButton();
    Button getDropButton();
    Image getImage();
  }

  private ICroppedTileView view;
  private ManageImagesListener listener;
  private File rootFile;

  private String lastPathValue;
  private boolean setLastValue;

  private static EventHandler<ActionEvent> fileSystemHandler;

  public CroppedTileController(ICroppedTileView view, File rootFile, ManageImagesListener listener) {
    this.view = view;
    this.rootFile = rootFile;
    this.listener = listener;
  }

  @Override
  public void bind() {
    addListeners();
    setLastValue = true;
    view.getPathTextField().setText(rootFile.getAbsolutePath());
    view.getPathTextField().setTooltip(new Tooltip(rootFile.getAbsolutePath()));
  }

  private void addListeners() {
    view.getNameTextField().textProperty().addListener((observable1, oldValue1, newValue) -> {
      view.getSaveButton().setDisable(!isValidSelection(newValue, view.getPathTextField().getText()));
    });

    view.getPathTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      view.getSaveButton().setDisable(!isValidSelection(view.getNameTextField().getText(), newValue));
      if (setLastValue)
        lastPathValue = newValue;
      if (view.getPathTextField().getTooltip() != null)
        view.getPathTextField().getTooltip().setText(newValue);
    });

    view.getPathButton().setOnAction(getFileSystemHandler());

    view.getSaveButton().setOnAction(event1 -> listener.saveCroppedImage(view));

    view.getDropButton().setOnAction(event -> listener.dropCroppedTileView(view));
  }

  private EventHandler<ActionEvent> getFileSystemHandler() {
    if (fileSystemHandler == null)
      fileSystemHandler = event -> {
        OkCancelDialog dialog = new OkCancelDialog("Choose Tile Path", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true);
        SystemFilesView filesView = new SystemFilesView(dialog.getOkButton(), rootFile, true, null);
        dialog.setContent(filesView.asNode());
        dialog.show();
      };
    return fileSystemHandler;
  }

  private boolean isValidSelection(String name, String path) {
    return name != null && (name.matches("^[a-zA-Z0-9[-_]]+") || (name.matches("[a-zA-Z0-9[-_]]+\\..*") && FileExtensionUtil.isImageFile(name))) &&
            path != null && path.contains(rootFile.getAbsolutePath());
  }
}

package mapEditor.application.main_part.manage_images.cropped_tiles.detailed_view;

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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public class CroppedTilesDetailedController implements Controller {

  public interface ICroppedTileDetailedView extends View {
    TextField getNameTextField();
    TextField getPathTextField();
    Button getPathButton();
    Button getSaveButton();
    Button getDropButton();
    Image getImage();
  }

  private List<ICroppedTileDetailedView> views;
  private ManageImagesListener listener;
  private File rootFile;

  private String lastPathValue;

  private static EventHandler<ActionEvent> fileSystemHandler;

  public CroppedTilesDetailedController(File rootFile, ManageImagesListener listener) {
    this.rootFile = rootFile;
    this.listener = listener;
  }

  @Override
  public void bind() {
    views = new ArrayList<>();
  }

  private void addListeners(ICroppedTileDetailedView view) {
    view.getNameTextField().textProperty().addListener((observable1, oldValue1, newValue) -> {
      view.getSaveButton().setDisable(!isValidSelection(newValue, view.getPathTextField().getText()));
    });

    view.getPathTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      view.getSaveButton().setDisable(!isValidSelection(view.getNameTextField().getText(), newValue));
      if (view.getPathTextField().getTooltip() != null)
        view.getPathTextField().getTooltip().setText(newValue);
    });

    view.getPathButton().setOnAction(getFileSystemHandler());

    view.getSaveButton().setOnAction(event1 -> listener.saveCroppedImage(view));

    view.getDropButton().setOnAction(event -> {
      views.remove(view);
      listener.dropCroppedTileView(view);
    });
  }

  public void addView(ICroppedTileDetailedView view) {
    views.add(view);
    addListeners(view);
    view.getPathTextField().setText(rootFile.getAbsolutePath());
    view.getPathTextField().setTooltip(new Tooltip(rootFile.getAbsolutePath()));
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

  public List<Image> getImages() {
    return views.stream().map(ICroppedTileDetailedView::getImage).collect(Collectors.toList());
  }
}

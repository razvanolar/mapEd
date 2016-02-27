package mapEditor.application.main_part.main_app_toolbars.main_toolbar.create_maps;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;
import mapEditor.application.main_part.app_utils.models.MapDetailsModel;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.SystemParameters;

import java.io.File;

/**
 *
 * Created by razvanolar on 25.02.2016.
 */
public class CreateMapController implements Controller {

  public interface ICreateMapView extends View {
    TextField getNameTextField();
    TextField getPathTextField();
    Button getPathButton();
    Spinner<Integer> getRowSpinner();
    Spinner<Integer> getColumnSpinner();
    ColorPicker getBackgroundColorPicker();
    ColorPicker getGridColorPicker();
  }

  private ICreateMapView view;
  private Node completeSelectionNode;
  private File rootFile;

  public CreateMapController(ICreateMapView view, Node completeSelectionNode, File rootFile) {
    this.view = view;
    this.completeSelectionNode = completeSelectionNode;
    this.rootFile = rootFile;
  }

  @Override
  public void bind() {
    addListeners();
    view.getPathTextField().setText(rootFile.getAbsolutePath());
  }

  private void addListeners() {
    view.getNameTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      completeSelectionNode.setDisable(!isValidSelection(newValue, view.getPathTextField().getText()));
    });

    view.getPathTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      completeSelectionNode.setDisable(!isValidSelection(view.getNameTextField().getText(), newValue));
    });

    view.getPathButton().setOnAction(event -> {
      OkCancelDialog dialog = new OkCancelDialog("Choose Path", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true);

      SystemFilesView filesView = new SystemFilesView(dialog.getOkButton(), rootFile, true, null);

      dialog.getOkButton().setOnAction(event1 -> {
        view.getPathTextField().setText(filesView.getSelectedPath());
        dialog.close();
      });
      dialog.setContent(filesView.asNode());
      dialog.show();
    });
  }

  private boolean isValidSelection(String name, String path) {
    return isValidMapName(name) && isValidPath(path);
  }

  private boolean isValidMapName(String name) {
    if (StringValidator.isNullOrEmpty(name))
      return false;
    KnownFileExtensions ext = FileExtensionUtil.getFileExtension(name);
    int firstIndex = name.indexOf('.');
    int lastIndex = name.lastIndexOf('.');
    return firstIndex == lastIndex &&
            (name.matches("[a-zA-Z0-9[_-]]+$") || name.matches("[a-zA-Z0-9[_-]]+\\.map$")) &&
            (ext == KnownFileExtensions.NONE || ext == KnownFileExtensions.MAP);
  }

  private boolean isValidPath(String path) {
    return !StringValidator.isNullOrEmpty(path) && path.contains(rootFile.getAbsolutePath());
  }

  public MapDetailsModel getMapDetailsModel() {
    String name = view.getNameTextField().getText();
    if (!name.endsWith(SystemParameters.MAP_FILE_EXT))
      name += SystemParameters.MAP_FILE_EXT;
    return new MapDetailsModel(name,
            view.getPathTextField().getText(),
            view.getRowSpinner().getValue(),
            view.getColumnSpinner().getValue(),
            view.getBackgroundColorPicker().getValue(),
            view.getGridColorPicker().getValue(),
            AppParameters.CURRENT_PROJECT.getMapType());
  }
}

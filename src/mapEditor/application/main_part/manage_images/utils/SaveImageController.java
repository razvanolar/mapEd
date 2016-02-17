package mapEditor.application.main_part.manage_images.utils;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.io.File;

/**
 *
 * Created by razvanolar on 13.02.2016.
 */
public class SaveImageController implements Controller {

  public enum ISaveImageViewState {
    NAME, PATH, BOTH
  }

  public interface ISaveImageView extends View {
    void setState(ISaveImageViewState state);
    ISaveImageViewState getState();
    TextField getNameTextField();
    TextField getPathTextField();
    Button getPathButton();
  }

  private ISaveImageView view;
  private KnownFileExtensions imageExtension;
  private File rootFile;
  private Node completeSelectionNode;

  public SaveImageController(ISaveImageView view, KnownFileExtensions imageExtension, File rootFile, Node completeSelectionNode) {
    this.view = view;
    this.imageExtension = imageExtension;
    this.rootFile = rootFile;
    this.completeSelectionNode = completeSelectionNode;
  }

  @Override
  public void bind() {
    addListeners();
    view.getNameTextField().setText("*" + imageExtension.getExtension());
    view.getPathTextField().setText(rootFile.getAbsolutePath());
    view.getPathTextField().setTooltip(new Tooltip(rootFile.getAbsolutePath()));
  }

  private void addListeners() {
    view.getPathTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      completeSelectionNode.setDisable(!isValidSelection(view.getNameTextField().getText(), newValue));
      if (view.getPathTextField().getTooltip() != null)
        view.getPathTextField().getTooltip().setText(newValue);
    });

    view.getNameTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      completeSelectionNode.setDisable(!isValidSelection(newValue, view.getPathTextField().getText()));
    });

    view.getPathButton().setOnAction(event -> doOnPathButtonSelection());
  }

  private void doOnPathButtonSelection() {
    OkCancelDialog dialog = new OkCancelDialog("Choose Image Directory", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true);
    SystemFilesView filesView = new SystemFilesView(dialog.getOkButton(), rootFile, true, null);
    dialog.setContent(filesView.asNode());

    dialog.getOkButton().setOnAction(event -> {
      view.getPathTextField().setText(filesView.getSelectedPath());
      dialog.close();
    });

    dialog.show();
  }

  private boolean isValidSelection(String name, String path) {
    ISaveImageViewState state = view.getState();
    boolean isNameValid = name != null && name.matches("^[a-zA-Z0-9[-_]]+" + imageExtension.forRegex());
    boolean isPathValid = path != null && path.contains(rootFile.getAbsolutePath());
    return state == ISaveImageViewState.NAME && isNameValid ||
            state == ISaveImageViewState.PATH && isPathValid ||
            state == ISaveImageViewState.BOTH && isNameValid && isPathValid;
  }

  public void validateInput() {
    completeSelectionNode.setDisable(!isValidSelection(view.getNameTextField().getText(), view.getPathTextField().getText()));
  }

  public ISaveImageView getView() {
    return view;
  }
}

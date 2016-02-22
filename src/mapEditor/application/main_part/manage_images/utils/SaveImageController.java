package mapEditor.application.main_part.manage_images.utils;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageModel;
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

  private ImageModel imageModel;

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

  public boolean isValidSelection(String name, String path) {
    ISaveImageViewState state = view.getState();
    boolean isNameValid = isValidName(name);
    boolean isPathValid = isValidPath(path);
    return state == ISaveImageViewState.NAME && isNameValid ||
            state == ISaveImageViewState.PATH && isPathValid ||
            state == ISaveImageViewState.BOTH && isNameValid && isPathValid;
  }

  private boolean isValidName(String name) {
    return !StringValidator.isNullOrEmpty(name) && name.matches("^[a-zA-Z0-9[-_()]]+" + imageExtension.forRegex());
  }

  private boolean isValidPath(String path) {
    return !StringValidator.isNullOrEmpty(path) && path.contains(rootFile.getAbsolutePath());
  }

  public void validateInput() {
    completeSelectionNode.setDisable(!isValidSelection(view.getNameTextField().getText(), view.getPathTextField().getText()));
  }

  public ISaveImageView getView() {
    return view;
  }

  public String getName() {
    return view.getNameTextField().getText();
  }

  public String getPath() {
    return view.getPathTextField().getText();
  }

  public void setName(String name) {
    if (isValidName(name))
      view.getNameTextField().setText(name);
    else
      view.getNameTextField().setText("*" + imageExtension.getExtension());
  }

  public void setPath(String path) {
    if (isValidPath(path))
      view.getPathTextField().setText(path);
    else
      view.getPathTextField().setText(rootFile.getAbsolutePath());
  }

  public ImageModel getImageModel() {
    return imageModel;
  }

  public void setImageModel(ImageModel imageModel) {
    this.imageModel = imageModel;
    if (imageModel != null) {
      KnownFileExtensions ext = FileExtensionUtil.getFileExtension(imageModel.getImageName());
      if (ext != KnownFileExtensions.UNKNOWN)
        imageExtension = ext;
      setName(imageModel.getImageName());
      setPath(imageModel.getImagePath());
    }
  }

  public boolean isOverwriteActive() {
    return imageModel != null &&
            imageModel.getImageName().equals(view.getNameTextField().getText()) &&
            imageModel.getImagePath().equals(view.getPathTextField().getText());
  }
}

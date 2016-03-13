package mapEditor.application.main_part.manage_images.manage_tiles;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.views.dialogs.*;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.manage_images.manage_tiles.utils.EditSelectableTileListener;
import mapEditor.application.main_part.manage_images.manage_tiles.utils.EditSelectableTileView;
import mapEditor.application.main_part.manage_images.manage_tiles.utils.ImageModelWrapper;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.results.SaveImagesResult;
import mapEditor.application.repo.statuses.SaveImagesStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 11.03.2016.
 */
public class ManageEditTilesController implements Controller, EditSelectableTileListener {

  public interface IManageEditTilesView extends View {
    void addTile(Node node);
    void removeTile(Node node);
    Slider getImageHueSlider();
    Slider getImageBrightnessSlider();
    Slider getImageContrastSlider();
    Slider getImageSaturationSlider();
    TextField getNameTextField();
    TextField getPathTextField();
    TextField getAllPathTextField();
    Button getPathButton();
    Button getLoadTilesButton();
    Button getAllPathButton();
    CheckBox getUsePathForAllCheckBox();
  }

  private IManageEditTilesView view;
  private List<File> tileFiles;
  private Button saveButton;
  private Window parentWindow;
  private ColorAdjust colorAdjust;
  private List<EditSelectableTileView> tileViews;
  private EditSelectableTileView selectedTileView;
  private File root;

  public ManageEditTilesController(IManageEditTilesView view, List<File> tileFiles, Button saveButton, Window parentWindow) {
    this.view = view;
    this.tileFiles = tileFiles;
    this.saveButton = saveButton;
    this.parentWindow = parentWindow;
  }

  @Override
  public void bind() {
    colorAdjust = new ColorAdjust(0, 0, 0, 0);
    tileViews = new ArrayList<>();
    root = AppParameters.CURRENT_PROJECT.getTilesFile();
    loadTiles();
    setEnableFields(false);
    view.getUsePathForAllCheckBox().setSelected(true);
    view.getAllPathTextField().setText(root.getAbsolutePath());
    addListeners();
  }

  private void addListeners() {
    colorAdjust.hueProperty().bind(view.getImageHueSlider().valueProperty());
    colorAdjust.saturationProperty().bind(view.getImageSaturationSlider().valueProperty());
    colorAdjust.brightnessProperty().bind(view.getImageBrightnessSlider().valueProperty());
    colorAdjust.contrastProperty().bind(view.getImageContrastSlider().valueProperty());

    ChangeListener<Number> numberChangeListener = (observable, oldValue, newValue) -> {
      for (EditSelectableTileView tileView : tileViews)
        tileView.setStyle(colorAdjust);
    };
    colorAdjust.hueProperty().addListener(numberChangeListener);
    colorAdjust.saturationProperty().addListener(numberChangeListener);
    colorAdjust.brightnessProperty().addListener(numberChangeListener);
    colorAdjust.contrastProperty().addListener(numberChangeListener);

    view.getNameTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      if (selectedTileView != null)
        selectedTileView.getModel().setName(newValue);
    });
    view.getPathTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      Tooltip tooltip = view.getPathTextField().getTooltip();
      if (tooltip == null) {
        tooltip = new Tooltip();
        view.getPathTextField().setTooltip(tooltip);
      }
      tooltip.setText(newValue);
      if (selectedTileView != null)
        selectedTileView.getModel().setPath(newValue);
    });

    view.getAllPathTextField().textProperty().addListener((observable1, oldValue1, newValue1) -> {
      saveButton.setDisable(isUseAllSelected() && !isValidPath(newValue1));
    });

    view.getPathButton().setOnAction(event -> onPathButtonSelection(view.getPathTextField()));
    view.getAllPathButton().setOnAction(event -> onPathButtonSelection(view.getAllPathTextField()));

    view.getUsePathForAllCheckBox().selectedProperty().addListener((observable, oldValue, newValue) -> {
      setEnableFields(!newValue);
    });

    saveButton.setOnAction(event -> onSaveButtonSelection());
  }

  private void onPathButtonSelection(TextField field) {
    OkCancelDialog dialog = new OkCancelDialog("Tiles", StageStyle.UTILITY, Modality.WINDOW_MODAL, true, true, parentWindow);

    SystemFilesView filesView = new SystemFilesView(dialog.getOkButton(), AppParameters.CURRENT_PROJECT.getTilesFile(), true, null);
    dialog.getOkButton().setOnAction(event1 -> {
      field.setText(filesView.getSelectedPath());
      dialog.close();
    });

    dialog.setContent(filesView.asNode());
    dialog.show();
  }

  private void onSaveButtonSelection() {
    if (tileViews == null || tileViews.isEmpty()) {
      parentWindow.hide();
      return;
    }

    boolean usePathForAll = isUseAllSelected();
    String selectedPath = view.getAllPathTextField().getText();
    if (usePathForAll && !isValidPath(selectedPath)) {
      Dialog.showWarningDialog(null, "Selected path is not a valid one!", parentWindow);
      return;
    }

    List<ImageModel> models = new ArrayList<>();
    for (EditSelectableTileView tileView : tileViews) {
      ImageModelWrapper wrapper = tileView.getModel();
      String name = wrapper.getName();
      String path = wrapper.getPath();
      if (!isValidName(name)) {
        Dialog.showWarningDialog(null, name + " is not a valid tile name!", parentWindow);
        return;
      }
      if (!usePathForAll && !isValidPath(path)) {
        Dialog.showWarningDialog(null, path + " is not a valid path for " + name, parentWindow);
        return;
      } else if (usePathForAll)
        wrapper.setPath(selectedPath);
      ImageModel model = wrapper.computeModel();
      model.setImage(tileView.getImage());
      models.add(model);
    }

    MapEditorController.getInstance().maskView();
    SaveImagesResult result = MapEditorController.getInstance().getRepoController().saveImages(models);
    MapEditorController.getInstance().unmaskView();
    handleSaveImagesResult(result);
  }

  private void handleSaveImagesResult(SaveImagesResult result) {
    // All the tiles were saved successfully; Display an information message and close the dialog
    if (result.getStatus() == SaveImagesStatus.COMPLETE) {
      parentWindow.hide();
      Dialog.showInformDialog(null, result.getStatus().getMessage());
      return;
    }

    // Only a part of tiles where saved; Keep only the unsaved tiles and remove the rest
    if (result.getStatus() == SaveImagesStatus.PARTIAL) {
      Dialog.showWarningDialog(null, result.getStatus().getMessage(), parentWindow);
      if (result.getUnsavedImages() == null)
        return;
      for (EditSelectableTileView tileView : tileViews) {
        ImageModel tileModel = tileView.getModel().getModel();
        for (ImageModel model : result.getUnsavedImages()) {
          if (model.equals(tileModel)) {
            view.removeTile(tileView.asNode());
            break;
          }
        }
      }
    }
  }

  private void loadTiles() {
    if (tileFiles == null || tileFiles.isEmpty())
      return;
    int cellSize = AppParameters.CURRENT_PROJECT.getCellSize();
    for (File file : tileFiles) {
      try {
        ImageModel tile = ImageProvider.getImageModel(file);
        if (tile != null && tile.getImage() != null && tile.getImage().getWidth() == cellSize && tile.getImage().getHeight() == cellSize) {
          ImageModelWrapper wrapper = new ImageModelWrapper(tile);
          EditSelectableTileView editSelectableTileView = new EditSelectableTileView(wrapper, cellSize, this);
          tileViews.add(editSelectableTileView);
          view.addTile(editSelectableTileView.asNode());
        }
      } catch (Exception ex) {
        System.out.println("*** ManageEditTilesController - Unable to load tile. Path: " + file.getAbsolutePath());
      }
    }
  }

  @Override
  public void selectedTileChanged(EditSelectableTileView tileView) {
    selectedTileView = tileView;
    if (selectedTileView == null) {
      view.getNameTextField().setText("");
      view.getPathTextField().setText("");
      return;
    }
    view.getNameTextField().setText(selectedTileView.getModel().getName());
    if (isUseAllSelected())
      view.getPathTextField().setText(view.getAllPathTextField().getText());
    else
      view.getPathTextField().setText(selectedTileView.getModel().getPath());
  }

  private void setEnableFields(boolean value) {
    view.getNameTextField().setDisable(!value);
    view.getPathTextField().setDisable(!value);
    view.getPathButton().setDisable(!value);
  }

  private boolean isUseAllSelected() {
    return view.getUsePathForAllCheckBox().isSelected();
  }

  private boolean isValidPath(String path) {
    return path != null && path.contains(root.getAbsolutePath());
  }

  private boolean isValidName(String name) {
    return FileExtensionUtil.isImageFile(name);
  }
}

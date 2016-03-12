package mapEditor.application.main_part.manage_images.manage_tiles;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.manage_images.manage_tiles.utils.EditSelectableTileListener;
import mapEditor.application.main_part.manage_images.manage_tiles.utils.EditSelectableTileView;
import mapEditor.application.main_part.manage_images.manage_tiles.utils.ImageModelWrapper;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 11.03.2016.
 */
public class ManageEditTilesController implements Controller, EditSelectableTileListener {

  public interface IManageEditTilesView extends View {
    void addTile(Node imageView);
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
    loadTiles();
    setEnableFields(false);
    view.getUsePathForAllCheckBox().setSelected(true);
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

    view.getPathTextField().textProperty().addListener((observable1, oldValue1, newValue1) -> {
      Tooltip tooltip = view.getPathTextField().getTooltip();
      if (tooltip == null) {
        tooltip = new Tooltip();
        view.getPathTextField().setTooltip(tooltip);
      }
      tooltip.setText(newValue1);
    });

    view.getPathButton().setOnAction(event -> onPathButtonSelection(view.getPathTextField()));
    view.getAllPathButton().setOnAction(event -> onPathButtonSelection(view.getAllPathTextField()));

    view.getUsePathForAllCheckBox().selectedProperty().addListener((observable, oldValue, newValue) -> {
      setEnableFields(!newValue);
    });
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
    view.getPathTextField().setText(selectedTileView.getModel().getPath());
  }

  private void setEnableFields(boolean value) {
    view.getNameTextField().setDisable(!value);
    view.getPathTextField().setDisable(!value);
    view.getPathButton().setDisable(!value);
  }
}

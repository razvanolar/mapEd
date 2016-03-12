package mapEditor.application.main_part.manage_images.manage_tiles;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.manage_images.manage_tiles.utils.EditSelectableTileView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 11.03.2016.
 */
public class ManageEditTilesController implements Controller {

  public interface IManageEditTilesView extends View {
    void addTile(Node imageView);
    Slider getImageHueSlider();
    Slider getImageBrightnessSlider();
    Slider getImageContrastSlider();
    Slider getImageSaturationSlider();
    TextField getNameTextField();
    TextField getPathTextField();
    Button getPathButton();
    CheckBox getOverwriteCheckBox();
  }

  private IManageEditTilesView view;
  private List<File> tileFiles;
  private Button saveButton;
  private Window parentWindow;
  private ColorAdjust colorAdjust;
  private List<EditSelectableTileView> tileViews;

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

    view.getPathButton().setOnAction(event -> {
      OkCancelDialog dialog = new OkCancelDialog("Tiles", StageStyle.UTILITY, Modality.WINDOW_MODAL, true, true, parentWindow);

      SystemFilesView filesView = new SystemFilesView(dialog.getOkButton(), AppParameters.CURRENT_PROJECT.getTilesFile(), true, null);
      dialog.getOkButton().setOnAction(event1 -> {

      });

      dialog.setContent(filesView.asNode());
      dialog.show();
    });
  }

  private void loadTiles() {
    if (tileFiles == null || tileFiles.isEmpty())
      return;
    int cellSize = AppParameters.CURRENT_PROJECT.getCellSize();
    for (File file : tileFiles) {
      try {
        Image tile = ImageProvider.getImage(file);
        if (tile != null && tile.getWidth() == cellSize && tile.getHeight() == cellSize) {
          EditSelectableTileView editSelectableTileView = new EditSelectableTileView(new ImageView(tile), cellSize);
          tileViews.add(editSelectableTileView);
          view.addTile(editSelectableTileView.asNode());
        }
      } catch (Exception ex) {
        System.out.println("*** ManageEditTilesController - Unable to load tile. Path: " + file.getAbsolutePath());
      }
    }
  }
}

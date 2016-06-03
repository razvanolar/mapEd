package mapEditor.application.main_part.manage_maps.primary_map.tmx_export;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.*;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 27.05.2016.
 */
public class TmxExportController implements Controller {

  public interface ITmxExportView extends View {
    TextField getNameTextField();
    TextField getPathTextField();
    Button getPathButton();
  }

  private ITmxExportView view;
  private MapDetail mapDetail;
  private Window owner;
  private Button completeSelectionButton;

  public TmxExportController(ITmxExportView view, MapDetail mapDetail, Window owner, Button completeSelectionButton) {
    this.view = view;
    this.mapDetail = mapDetail;
    this.owner = owner;
    this.completeSelectionButton = completeSelectionButton;
  }

  @Override
  public void bind() {
    completeSelectionButton.setDisable(true);

    view.getPathTextField().setText(AppParameters.CURRENT_PROJECT.getHomePath());

    view.getPathButton().setOnAction(event -> {
      OkCancelDialog dialog = new OkCancelDialog("Choose TMX Path", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true, true, owner);
      SystemFilesView filesView = new SystemFilesView(dialog.getOkButton(), null, false, AppParameters.CURRENT_PROJECT.getHomePath());
      dialog.setContent(filesView.asNode());

      dialog.getOkButton().setOnAction(event1 -> {
        view.getPathTextField().setText(filesView.getSelectedPath());
        dialog.close();
      });

      dialog.show();
    });

    view.getNameTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      completeSelectionButton.setDisable(!isValidSelection(newValue, view.getPathTextField().getText()));
    });

    view.getPathTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      completeSelectionButton.setDisable(!isValidSelection(view.getNameTextField().getText(), newValue));
    });
  }

  /**
   * Compute the distinct tiles list, creates a tileset with them and then call the repo in order to save the tileset.
   * @return tileset name if it was successfully saved; null otherwise
   * @throws Exception
   */
  public String constructAndSaveTileset() throws Exception {
    String name = view.getNameTextField().getText();
    String path = view.getPathTextField().getText();
    if (!isValidSelection(name, path))
      return null;

    // compute the distinct tiles list
    List<ImageModel> distinctTiles = new ArrayList<>();
    CustomMap<LayerModel, CustomMap<ImageModel, List<CellModel>>> layersTilesMap = mapDetail.getMapTilesInfo().getLayersTilesMap();
    for (LayerModel layer : layersTilesMap.keys()) {
      CustomMap<ImageModel, List<CellModel>> imageModelListMap = layersTilesMap.get(layer);
      imageModelListMap.keys().stream().filter(imageModel -> !distinctTiles.contains(imageModel)).forEach(imageModel -> {
        imageModel.setId(distinctTiles.size() + 1);
        distinctTiles.add(imageModel);
      });
    }

    long size = Math.round(Math.sqrt((double) distinctTiles.size()));
    if (size * size < distinctTiles.size())
      size ++;
    int width = (int) size;
    int height = (int) size;

    int cellSize = AppParameters.CURRENT_PROJECT.getCellSize();

    // create the tileset
    WritableImage writableImage = new WritableImage(width * cellSize, height * cellSize);
    PixelWriter writer = writableImage.getPixelWriter();
    int x = 0;
    int y = 0;
    for (ImageModel imageModel : distinctTiles) {
      Image image = imageModel.getImage();
      if (image.getWidth() != cellSize || image.getHeight() != cellSize) {
        System.out.println("-> Unable to use " + imageModel.getPath() +", sizes don't match.");
        continue;
      }
      PixelReader pixelReader = image.getPixelReader();
      for (int i=0; i<cellSize; i++) {
        for (int j=0; j<cellSize; j++) {
          writer.setArgb(x * cellSize + j, y * cellSize + i, pixelReader.getArgb(j, i));
        }
      }

      if (x == width - 1) {
        x = 0;
        y ++;
      } else {
        x ++;
      }
    }

    name = FileExtensionUtil.getNameWithoutExtension(name, KnownFileExtensions.TMX);
    name = FileExtensionUtil.getNameWithExtension(name, KnownFileExtensions.PNG);
    return MapEditorController.getInstance().getRepoController().saveImage(writableImage, path, name, false);
  }

  public boolean exportMapToTMX(String tilesetName) throws Exception {
    String name = view.getNameTextField().getText();
    String path = view.getPathTextField().getText();
    return isValidSelection(name, path) && MapEditorController.getInstance().getRepoController().exportMapToTMX(mapDetail, path, name, tilesetName);
  }

  private boolean isValidSelection(String name, String path) {
    return isValidName(name) && isValidPath(path);
  }

  private boolean isValidName(String name) {
    return StringValidator.isValidFileName(name) || FileExtensionUtil.isTmxFile(name);
  }

  private boolean isValidPath(String path) {
    return !StringValidator.isNullOrEmpty(path);
  }
}

package mapEditor.application.main_part.manage_images.manage_tile_sets.utils.load_views;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Window;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.FilesLoader;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 22.03.2016.
 */
public class LoadTileSetsController implements Controller, SelectableTileSetFieldListener {

  public interface ILoadTileSetsView extends View {
    void addField(Region node);
    void removeField(Region node);
    void setPath(String path);
  }

  private ILoadTileSetsView view;
  private Window parentWindow;
  private Button loadButton;
  private Button completeSelectionButton;
  private String relativePath;

  private List<SelectableTilesetFieldView> fields;

  public LoadTileSetsController(ILoadTileSetsView view, Window parentWindow, Button loadButton,
                                Button completeSelectionButton, String relativePath) {
    this.view = view;
    this.parentWindow = parentWindow;
    this.loadButton = loadButton;
    this.completeSelectionButton = completeSelectionButton;
    this.relativePath = relativePath;
  }

  @Override
  public void bind() {
    completeSelectionButton.setDisable(true);
    fields = new ArrayList<>();
    view.setPath(relativePath);
    addListeners();
  }

  private void addListeners() {
    loadButton.setOnAction(event -> FilesLoader.getInstance().loadFiles(files -> {
      if (files == null)
        return null;

      for (File file : files) {
        if (checkIfFileExists(file))
          continue;
        Image image = ImageProvider.getImage(file);
        SelectableTilesetFieldView field = new SelectableTilesetFieldView(file, image, this);
        view.addField(field.asNode());
        fields.add(field);
      }
      completeSelectionButton.setDisable(!isValidSelection());
      return null;
    }, parentWindow));
  }

  private boolean isValidSelection() {
    for (SelectableTilesetFieldView field : fields) {
      if (!FileExtensionUtil.isImageFile(field.getName()))
        return false;
    }
    return true;
  }

  private boolean checkIfFileExists(File file) {
    if (file == null)
      return false;
    for (SelectableTilesetFieldView field : fields) {
      if (field.getFile().equals(file))
        return true;
    }
    return false;
  }

  @Override
  public void deleteField(SelectableTilesetFieldView field) {
    view.removeField(field.asNode());
    fields.remove(field);
  }

  @Override
  public void nameChanged() {
    completeSelectionButton.setDisable(!isValidSelection());
  }

  public Map<File, String> getFilesMap() {
    Map<File, String> filesMap = new HashMap<>();
    for (SelectableTilesetFieldView field : fields) {
      filesMap.put(field.getFile(), field.getName());
    }
    return filesMap;
  }
}

package mapEditor.application.main_part.manage_images.manage_tiles;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.io.File;
import java.util.List;

/**
 *
 * Created by razvanolar on 11.03.2016.
 */
public class ManageEditTilesController implements Controller {

  public interface IManageEditTilesView extends View {
    void addTile(ImageView imageView);
  }

  private IManageEditTilesView view;
  private List<File> tileFiles;
  private Button saveButton;

  public ManageEditTilesController(IManageEditTilesView view, List<File> tileFiles, Button saveButton) {
    this.view = view;
    this.tileFiles = tileFiles;
    this.saveButton = saveButton;
  }

  @Override
  public void bind() {
    loadTiles();
  }

  private void loadTiles() {
    if (tileFiles == null || tileFiles.isEmpty())
      return;
    int cellSize = AppParameters.CURRENT_PROJECT.getCellSize();
    for (File file : tileFiles) {
      try {
        Image tile = ImageProvider.getImage(file);
        if (tile != null && tile.getWidth() == cellSize && tile.getHeight() == cellSize) {
          view.addTile(new ImageView(tile));
        }
      } catch (Exception ex) {
        System.out.println("*** ManageEditTilesController - Unable to load tile. Path: " + file.getAbsolutePath());
      }
    }
  }
}

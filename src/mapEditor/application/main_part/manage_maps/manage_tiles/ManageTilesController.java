package mapEditor.application.main_part.manage_maps.manage_tiles;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.app_utils.views.canvas.TilesCanvas;
import mapEditor.application.main_part.manage_maps.manage_tiles.create_tiles_tab.CreateTilesTabView;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.TilesTabContainer;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileListener;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileView;
import mapEditor.application.main_part.manage_maps.utils.TabType;
import mapEditor.application.main_part.types.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 23.01.2016.
 */
public class ManageTilesController implements Controller, SelectableTileListener {

  public interface IManageTilesView {
    TabPane getTabPane();
    Button getNewTabButton();
    Button getAddTilesButton();
    void addTab(Tab tab);
    Region asNode();
  }

  private IManageTilesView view;
  private SelectableTileView selectedTileView;

  public ManageTilesController(IManageTilesView view) {
    this.view = view;
  }

  public void bind() {
    addListeners();
  }

  private void addListeners() {
    view.getNewTabButton().setOnAction(event -> onAddNewTabSelection());
  }

  private void onAddNewTabSelection() {
    OkCancelDialog window = new OkCancelDialog("Add new tab", null, null, false);
    CreateTilesTabView tilesTabForm = new CreateTilesTabView();
    window.setContent(tilesTabForm.asNode());
    window.getOkButton().setOnAction(event -> {
        window.close();
    });
    window.show();
  }

  /**
   * Create a new tiles tab.
   * @param title
   * Tab title.
   * @param imageFiles
   * Image files.
   */
  public void addTilesTab(String title, List<File> imageFiles) {
    if (StringValidator.isNullOrEmpty(title) || imageFiles == null || imageFiles.isEmpty())
      return;

    List<ImageModel> images = new ArrayList<>();
    imageFiles.stream().filter(file -> FileExtensionUtil.isImageFile(file.getName())).forEach(file -> {
      ImageModel image = ImageProvider.getImageModel(file);
      if (image != null)
        images.add(image);
    });

    TilesTabContainer tilesTabContainer = new TilesTabContainer(true);
    for (ImageModel image : images)
      tilesTabContainer.addTile(new SelectableTileView(image, true, this));

    Tab tab = new Tab(title, tilesTabContainer.asNode());
    tab.setUserData(tilesTabContainer);
    view.addTab(tab);
  }

  @Override
  public void selectedTileChanged(SelectableTileView selectedView) {
    if (selectedTileView == null) {
      selectedTileView = selectedView;
    } else if (selectedTileView != selectedView) {
      selectedTileView.unselect();
      selectedTileView = selectedView;
    }
  }
}

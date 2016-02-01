package mapEditor.application.manage_tiles;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import mapEditor.application.app_utils.inputs.StringValidator;
import mapEditor.application.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.app_utils.views.canvas.TilesCanvas;
import mapEditor.application.manage_tiles.utils.NewTilesTabForm;
import mapEditor.application.types.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by razvanolar on 23.01.2016.
 */
public class ManageTilesController implements Controller {

  public interface IManageTilesView {
    TabPane getTabPane();
    Button getNewTabButton();
    Button getAddTilesButton();
    void addTab(Tab tab);
    Region asNode();
  }

  private IManageTilesView view;
  private Map<String, TabModel> tabModelMap;

  public ManageTilesController(IManageTilesView view) {
    this.view = view;
  }

  public void bind() {
    tabModelMap = new HashMap<>();
    addListeners();
  }

  private void addListeners() {
    view.getNewTabButton().setOnAction(event -> onAddNewTabSelection());
  }

  private void onAddNewTabSelection() {
    OkCancelDialog window = new OkCancelDialog("Add new tab");
    NewTilesTabForm tilesTabForm = new NewTilesTabForm();
    window.setContent(tilesTabForm.asNode());
    window.getOkButton().setOnAction(event -> {
      String title = tilesTabForm.getTitle();
      if (!StringValidator.isNullOrEmpty(title) && !tabExists(title)) {
        addNewTilesTab(title, tilesTabForm.isCanvasTilesTab());
        window.close();
      }
    });
    window.show();
  }

  public boolean tabExists(String tabTitle) {
    return tabModelMap.containsKey(tabTitle);
  }

  private void addNewTilesTab(String tabTitle, boolean isCanvasTilesTab) {
    Tab tab = new Tab(tabTitle);
    tab.setClosable(true);
    if (isCanvasTilesTab) {
      tab.setContent(new TilesCanvas());
    } else {
      tab.setContent(new FlowPane());
    }

    tab.setOnClosed(event -> tabModelMap.remove(tab.getText()));

    view.addTab(tab);
    tabModelMap.put(tabTitle, new TabModel(isCanvasTilesTab));
  }
}

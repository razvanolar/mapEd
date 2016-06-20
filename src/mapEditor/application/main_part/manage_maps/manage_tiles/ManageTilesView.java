package mapEditor.application.main_part.manage_maps.manage_tiles;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.manage_maps.utils.AbstractTabView;

/**
 *
 * Created by razvanolar on 23.01.2016.
 */
public class ManageTilesView extends AbstractTabView implements ManageTilesController.IManageTilesView {

  private Button newTabButton;
  private Button addTilesButton;

  public ManageTilesView() {
    super();
    initGUI();
  }

  private void initGUI() {
    addTilesButton = new Button("Load");
    newTabButton = new Button("New Tab");

    toolBar.getItems().addAll(newTabButton, addTilesButton);

    mainContainer.setBottom(toolBar);
  }

  public void addTab(Tab tab) {
    tabPane.getTabs().add(tab);
  }

  public TabPane getTabPane() {
    return tabPane;
  }

  public Button getNewTabButton() {
    return newTabButton;
  }

  public Button getAddTilesButton() {
    return addTilesButton;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

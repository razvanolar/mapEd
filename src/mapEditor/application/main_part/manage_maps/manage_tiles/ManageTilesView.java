package mapEditor.application.main_part.manage_maps.manage_tiles;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

/**
 *
 * Created by razvanolar on 23.01.2016.
 */
public class ManageTilesView implements ManageTilesController.IManageTilesView {

  private BorderPane mainContainer;
  private TabPane tabPane;
  private Button newTabButton;
  private Button addTilesButton;

  public ManageTilesView() {
    initGUI();
  }

  private void initGUI() {
    tabPane = new TabPane();
    addTilesButton = new Button("Add Tiles");
    newTabButton = new Button("New Tab");
    mainContainer = new BorderPane();

    ToolBar toolBar = new ToolBar();
    toolBar.getItems().addAll(newTabButton, addTilesButton);

    mainContainer.setCenter(tabPane);
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

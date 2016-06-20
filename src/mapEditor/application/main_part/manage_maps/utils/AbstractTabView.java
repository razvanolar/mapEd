package mapEditor.application.main_part.manage_maps.utils;

import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

/**
 *
 * Created by razvanolar on 20.06.2016.
 */
public class AbstractTabView {

  protected BorderPane mainContainer;
  protected TabPane tabPane;
  protected ToolBar toolBar;

  protected AbstractTabView() {
    initGUI();
  }

  private void initGUI() {
    toolBar = new ToolBar();
    tabPane = new TabPane();
    mainContainer = new BorderPane(tabPane);

    mainContainer.setBottom(toolBar);
  }
}

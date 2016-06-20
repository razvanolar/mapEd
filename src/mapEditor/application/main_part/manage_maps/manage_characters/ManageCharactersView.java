package mapEditor.application.main_part.manage_maps.manage_characters;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.manage_maps.utils.AbstractTabView;

/**
 *
 * Created by razvanolar on 22.03.2016.
 */
public class ManageCharactersView extends AbstractTabView implements ManageCharactersController.IManageCharactersView {

  public ManageCharactersView() {
    initGUI();
  }

  private void initGUI() {

  }

  @Override
  public TabPane getTabPane() {
    return tabPane;
  }

  @Override
  public void addTab(Tab tab) {
    tabPane.getTabs().add(tab);
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

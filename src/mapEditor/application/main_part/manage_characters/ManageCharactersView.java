package mapEditor.application.main_part.manage_characters;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;

/**
 *
 * Created by razvanolar on 22.03.2016.
 */
public class ManageCharactersView implements ManageCharactersController.IManageCharactersView {

  private SplitPane mainContainer;

  public ManageCharactersView() {
    initGUI();
  }

  private void initGUI() {
    mainContainer = new SplitPane();
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

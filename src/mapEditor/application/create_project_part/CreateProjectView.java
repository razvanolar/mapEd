package mapEditor.application.create_project_part;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;

/**
 *
 * Created by razvanolar on 02.02.2016.
 */
public class CreateProjectView implements CreateProjectController.ICreateProjectView {

  private SplitPane splitPane;

  public CreateProjectView() {
    initGUI();
  }

  private void initGUI() {
    splitPane = new SplitPane();
  }

  @Override
  public Region asNode() {
    return splitPane;
  }
}

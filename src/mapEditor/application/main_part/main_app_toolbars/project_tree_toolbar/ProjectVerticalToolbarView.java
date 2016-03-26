package mapEditor.application.main_part.main_app_toolbars.project_tree_toolbar;

import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ProjectVerticalToolbarView implements ProjectVerticalToolbarController.IProjectVerticalToolbarView {

  private ToolBar toolBar;
  private ToggleButton projectButton;
  private ToggleButton structureButton;

  public ProjectVerticalToolbarView() {
    initGUI();
  }

  private void initGUI() {
    projectButton = new ToggleButton("Project");
    structureButton = new ToggleButton("Structure");
    toolBar = new ToolBar();

    Group group1 = new Group(projectButton);
    Group group2 = new Group(structureButton);

    projectButton.setRotate(-90);
    structureButton.setRotate(-90);

    toolBar.setOrientation(Orientation.VERTICAL);
    toolBar.getItems().addAll(group1, group2);
  }

  public ToggleButton getProjectButton() {
    return projectButton;
  }

  @Override
  public Region asNode() {
    return toolBar;
  }
}

package mapEditor.application.create_project_part;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import mapEditor.application.repo.models.LWProjectModel;

/**
 *
 * Created by razvanolar on 07.02.2016.
 */
public class ProjectContextMenu {

  private LWProjectModel project;

  private ContextMenu contextMenu;
  private MenuItem openMenuItem;
  private MenuItem renameMenuItem;
  private MenuItem deleteMenuItem;

  public ProjectContextMenu() {
    initGUI();
  }

  private void initGUI() {
    openMenuItem = new MenuItem("Open");
    renameMenuItem = new MenuItem("Rename");
    deleteMenuItem = new MenuItem("Delete");
    contextMenu = new ContextMenu(openMenuItem, renameMenuItem, deleteMenuItem);
  }

  public LWProjectModel getProject() {
    return project;
  }

  public void setProject(LWProjectModel project) {
    this.project = project;
  }

  public MenuItem getOpenMenuItem() {
    return openMenuItem;
  }

  public MenuItem getRenameMenuItem() {
    return renameMenuItem;
  }

  public MenuItem getDeleteMenuItem() {
    return deleteMenuItem;
  }

  public ContextMenu getContextMenu() {
    return contextMenu;
  }
}

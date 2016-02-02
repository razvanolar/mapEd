package mapEditor.application.main_part.main_app_toolbars.project_tree_toolbar;

import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ProjectVerticalToolbarController implements Controller {

  public interface IProjectVerticalToolbarView extends View {

  }

  private IProjectVerticalToolbarView view;

  public ProjectVerticalToolbarController(IProjectVerticalToolbarView view) {
    this.view = view;
  }

  public void bind() {
    addListeners();
  }

  private void addListeners() {

  }
}

package mapEditor.application.main_part.main_app_toolbars.project_tree_toolbar;

import javafx.scene.control.ToggleButton;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ProjectVerticalToolbarController implements Controller {

  public interface IProjectVerticalToolbarView extends View {
    ToggleButton getProjectButton();
  }

  private IProjectVerticalToolbarView view;

  public ProjectVerticalToolbarController(IProjectVerticalToolbarView view, boolean isProjectTreeSelected) {
    this.view = view;
    this.view.getProjectButton().setSelected(isProjectTreeSelected);
  }

  public void bind() {
    addListeners();
  }

  private void addListeners() {
    view.getProjectButton().selectedProperty().addListener((observable, oldValue, newValue) -> {
      MapEditorController.getInstance().setProjectTreeView(newValue);
    });
  }

  public boolean isShowProject() {
    return view.getProjectButton().isSelected();
  }
}

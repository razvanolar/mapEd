package mapEditor.application.main_part.menu_bar;

import javafx.scene.control.MenuItem;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 01.02.2016.
 */
public class MapEditorMenuBarController implements Controller {

  public interface IMapEditorMenuBarView extends View {
    MenuItem getCloseProjectMenuItem();
    MenuItem getExitMenuItem();
  }

  private IMapEditorMenuBarView view;

  public MapEditorMenuBarController(IMapEditorMenuBarView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    addListeners();
  }

  private void addListeners() {
    view.getCloseProjectMenuItem().setOnAction(event -> {
      boolean value = MapEditorController.getInstance().getRepoController().closeProject(AppParameters.CURRENT_PROJECT.getHomePath());
      if (value)
        MapEditorController.getInstance().closeApp();
    });

    view.getExitMenuItem().setOnAction(event -> {
      // TODO: validate before closing the app if there are unsaved parts
      MapEditorController.getInstance().closeApp();
    });
  }
}

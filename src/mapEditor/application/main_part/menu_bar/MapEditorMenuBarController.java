package mapEditor.application.main_part.menu_bar;

import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 01.02.2016.
 */
public class MapEditorMenuBarController implements Controller {

  public interface IMapEditorMenuBarView extends View {

  }

  private IMapEditorMenuBarView view;

  public MapEditorMenuBarController(IMapEditorMenuBarView view) {
    this.view = view;
  }

  @Override
  public void bind() {

  }
}

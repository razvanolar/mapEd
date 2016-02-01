package mapEditor.application.menu_bar;

import mapEditor.application.types.Controller;
import mapEditor.application.types.View;

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

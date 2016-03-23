package mapEditor.application.main_part.manage_characters;

import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 22.03.2016.
 */
public class ManageCharactersController implements Controller {

  public interface IManageCharactersView extends View {

  }

  private IManageCharactersView view;

  public ManageCharactersController(IManageCharactersView view) {
    this.view = view;
  }

  @Override
  public void bind() {

  }
}

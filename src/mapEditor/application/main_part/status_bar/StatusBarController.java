package mapEditor.application.main_part.status_bar;

import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 25.02.2016.
 */
public class StatusBarController implements Controller {

  public interface IStatusBarView extends View {

  }

  private IStatusBarView view;

  public StatusBarController(IStatusBarView view) {
    this.view = view;
  }

  @Override
  public void bind() {

  }
}

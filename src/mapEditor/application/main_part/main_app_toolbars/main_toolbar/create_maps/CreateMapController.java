package mapEditor.application.main_part.main_app_toolbars.main_toolbar.create_maps;

import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 25.02.2016.
 */
public class CreateMapController implements Controller {

  public interface ICreateMapView extends View {

  }

  private ICreateMapView view;

  public CreateMapController(ICreateMapView view) {
    this.view = view;
  }

  @Override
  public void bind() {

  }
}

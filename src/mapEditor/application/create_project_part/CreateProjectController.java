package mapEditor.application.create_project_part;

import javafx.scene.control.Button;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 02.02.2016.
 */
public class CreateProjectController implements Controller {

  public enum ICreateProjectViewState {
    CREATE, NONE
  }

  public interface ICreateProjectView extends View {
    void setState(ICreateProjectViewState state);
    Button getPathButton();
  }

  private ICreateProjectView view;

  public CreateProjectController(ICreateProjectView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    addListeners();
  }

  private void addListeners() {
    view.getPathButton().setOnAction(event -> {
      SystemFilesView systemFilesView = new SystemFilesView();
      systemFilesView.show();
    });
  }
}

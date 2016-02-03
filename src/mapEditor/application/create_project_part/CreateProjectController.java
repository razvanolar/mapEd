package mapEditor.application.create_project_part;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
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
    TextField getProjectNameTextField();
    TextField getProjectPathTextField();
    Button getPathButton();
  }

  private ICreateProjectView view;

  public CreateProjectController(ICreateProjectView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    view.getProjectNameTextField().setText(AppParameters.DEFAULT_PROJECT_NAME);
    view.getProjectPathTextField().setText(AppParameters.SYSTEM_FILES_VIEW_PATH);
    addListeners();
  }

  private void addListeners() {
    view.getPathButton().setOnAction(event -> {
      OkCancelDialog dialog = new OkCancelDialog("Choose Project Path", null, null, true);
      SystemFilesView systemFilesView = new SystemFilesView(dialog.getOkButton());
      dialog.setContent(systemFilesView.asNode());

      dialog.getOkButton().setOnAction(event1 -> {
        if (!StringValidator.isNullOrEmpty(systemFilesView.getSelectedPath()) && systemFilesView.isFolderSelected()) {
          view.getProjectPathTextField().setText(systemFilesView.getSelectedPath());
          dialog.close();
        }
      });

      dialog.show();
    });
  }

  private void showWarningDialog(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }
}

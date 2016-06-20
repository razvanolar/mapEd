package mapEditor.application.main_part.project_tree.open_image_dialogs;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 01.03.2016.
 */
public class NewEntitiesTabController implements Controller {

  public interface INewEntitiesTabView extends View {
    TextField getNameTextField();
  }

  private INewEntitiesTabView view;
  private Node completeSelectionNode;

  public NewEntitiesTabController(INewEntitiesTabView view, Node completeSelectionNode) {
    this.view = view;
    this.completeSelectionNode = completeSelectionNode;
  }

  @Override
  public void bind() {
    completeSelectionNode.setDisable(true);
    addListeners();
  }

  private void addListeners() {
    view.getNameTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      completeSelectionNode.setDisable(StringValidator.isNullOrEmpty(newValue) || !newValue.matches("[a-zA-Z0-9[-_]]+"));
    });
  }

  public String getTabName() {
    return view.getNameTextField().getText();
  }
}

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
public class NewTilesTabController implements Controller {

  public interface INewTilesTabView extends View {
    TextField getNameTextField();
  }

  private INewTilesTabView view;
  private Node completeSelectionNode;

  public NewTilesTabController(INewTilesTabView view, Node completeSelectionNode) {
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

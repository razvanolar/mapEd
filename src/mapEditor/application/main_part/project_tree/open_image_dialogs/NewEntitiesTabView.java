package mapEditor.application.main_part.project_tree.open_image_dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 *
 * Created by razvanolar on 01.03.2016.
 */
public class NewEntitiesTabView implements NewEntitiesTabController.INewEntitiesTabView {

  private GridPane mainContainer;
  private TextField nameTextField;

  public NewEntitiesTabView() {
    initGUI();
  }

  private void initGUI() {
    nameTextField = new TextField();
    mainContainer = new GridPane();

    nameTextField.setPrefWidth(250);

    mainContainer.setHgap(5);
    mainContainer.setPadding(new Insets(10));
    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.add(new Text("Tab Name: "), 0 ,0);
    mainContainer.add(nameTextField, 1, 0);
  }

  public TextField getNameTextField() {
    return nameTextField;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

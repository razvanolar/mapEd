package mapEditor.application.main_part.manage_images.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 *
 * Created by razvanolar on 13.02.2016.
 */
public class SaveImageView implements SaveImageController.ISaveImageView {

  private GridPane mainContainer;

  private TextField nameTextField;
  private TextField pathTextField;
  private Button pathButton;

  public SaveImageView() {
    initGUI();
  }

  private void initGUI() {
    nameTextField = new TextField();
    pathTextField = new TextField();
    pathButton = new Button("...");
    mainContainer = new GridPane();

    pathTextField.setPrefWidth(300);

    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setPadding(new Insets(5));
    mainContainer.setVgap(5);
    mainContainer.setHgap(5);
    mainContainer.add(new Text("Name : "), 0, 0);
    mainContainer.add(nameTextField, 1, 0, 2, 1);
    mainContainer.add(new Text("Path : "), 0, 1);
    mainContainer.add(pathTextField, 1, 1);
    mainContainer.add(pathButton, 2, 1);
  }

  public TextField getNameTextField() {
    return nameTextField;
  }

  public TextField getPathTextField() {
    return pathTextField;
  }

  public Button getPathButton() {
    return pathButton;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

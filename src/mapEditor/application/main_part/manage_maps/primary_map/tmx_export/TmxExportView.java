package mapEditor.application.main_part.manage_maps.primary_map.tmx_export;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 *
 * Created by razvanolar on 27.05.2016.
 */
public class TmxExportView implements TmxExportController.ITmxExportView {

  private BorderPane mainContainer;
  private TextField nameTextField;
  private TextField pathTextField;
  private Button pathButton;

  public TmxExportView() {
    initGUI();
  }

  private void initGUI() {
    nameTextField = new TextField();
    pathTextField = new TextField();
    pathButton = new Button("...");
    GridPane gridPane = new GridPane();
    mainContainer = new BorderPane(gridPane);

    gridPane.setAlignment(Pos.CENTER);
    gridPane.setVgap(5);
    gridPane.setHgap(5);
    gridPane.setPadding(new Insets(5));

    gridPane.add(new Text("Name: "), 0, 0);
    gridPane.add(nameTextField, 1, 0, 2, 1);
    gridPane.add(new Text("Path: "), 0, 1);
    gridPane.add(pathTextField, 1, 1);
    gridPane.add(pathButton, 2, 1);

    pathTextField.setMaxWidth(Double.MAX_VALUE);
    pathTextField.setMinWidth(250);
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

package mapEditor.application.main_part.main_app_toolbars.main_toolbar.create_maps;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.repo.SystemParameters;

/**
 *
 * Created by razvanolar on 25.02.2016.
 */
public class CreateMapView implements CreateMapController.ICreateMapView {

  private GridPane mainContainer;
  private TextField nameTextField;
  private TextField pathTextField;
  private Button pathButton;
  private Button infoButton;
  private Spinner<Integer> rowSpinner;
  private Spinner<Integer> columnSpinner;
  private ColorPicker backgroundColorPicker;
  private ColorPicker gridColorPicker;

  public CreateMapView() {
    initGUI();
  }

  private void initGUI() {
    nameTextField = new TextField();
    pathTextField = new TextField();
    pathButton = new Button("...");
    infoButton = new Button("?");
    rowSpinner = new Spinner<>(SystemParameters.MAP_MIN_SIZE_NUMBER, SystemParameters.MAP_MAX_SIZE_NUMBER, SystemParameters.MAP_DEFAULT_SIZE_NUMBER, 1);
    columnSpinner = new Spinner<>(SystemParameters.MAP_MIN_SIZE_NUMBER, SystemParameters.MAP_MAX_SIZE_NUMBER, SystemParameters.MAP_DEFAULT_SIZE_NUMBER, 1);
    backgroundColorPicker = new ColorPicker();
    gridColorPicker = new ColorPicker(Color.BLACK);
    mainContainer = new GridPane();

    pathTextField.setMinWidth(300);
    backgroundColorPicker.setMinWidth(150);
    gridColorPicker.setMinWidth(150);

    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setHgap(5);
    mainContainer.setVgap(5);
    mainContainer.setPadding(new Insets(10));
    mainContainer.add(new Text("Name : "), 0, 0);
    mainContainer.add(nameTextField, 1, 0, 4, 1);
    mainContainer.add(new Text("Path : "), 0, 1);
    mainContainer.add(pathTextField, 1, 1, 3, 1);
    mainContainer.add(pathButton, 4, 1);
    mainContainer.add(new Text("Rows : "), 0, 2);
    mainContainer.add(rowSpinner, 1, 2);
    mainContainer.add(new Text("Columns : "), 2, 2);
    mainContainer.add(columnSpinner, 3, 2);
    mainContainer.add(new Text("Bg Color : "), 0, 3);
    mainContainer.add(backgroundColorPicker, 1, 3);
    mainContainer.add(new Text("Grid Color : "), 2, 3);
    mainContainer.add(gridColorPicker, 3, 3);
    mainContainer.add(new Text("Type : "), 0, 4);
    mainContainer.add(new Text(AppParameters.CURRENT_PROJECT.getMapType().name()), 1, 4);
    mainContainer.add(infoButton, 4, 5);
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

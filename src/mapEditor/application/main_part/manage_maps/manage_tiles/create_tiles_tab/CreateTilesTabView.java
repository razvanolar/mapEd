package mapEditor.application.main_part.manage_maps.manage_tiles.create_tiles_tab;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 *
 * Created by razvanolar on 23.01.2016.
 */
public class CreateTilesTabView implements CreateTilesTabController.ICreateTilesTabView {

  private GridPane gridPane;
  private TextField tabTitleField;
  private RadioButton canvasTileTabButton;

  public CreateTilesTabView() {
    initGUI();
  }

  private void initGUI() {
    tabTitleField = new TextField();
    canvasTileTabButton = new RadioButton();
    RadioButton simpleTilesTabButton = new RadioButton();
    ToggleGroup toggleGroup = new ToggleGroup();
    gridPane = new GridPane();

    toggleGroup.getToggles().addAll(canvasTileTabButton, simpleTilesTabButton);
    canvasTileTabButton.setSelected(true);

    gridPane.setAlignment(Pos.CENTER);
    gridPane.setPadding(new Insets(5));
    gridPane.setVgap(5);
    gridPane.setHgap(5);

    gridPane.add(new Text("Tab Name : "), 0, 0);
    gridPane.add(tabTitleField, 1, 0);
    gridPane.add(new Text("Canvas Tiles : "), 0, 1);
    gridPane.add(canvasTileTabButton, 1, 1);
    gridPane.add(new Text("Simple Tiles : "), 0, 2);
    gridPane.add(simpleTilesTabButton, 1, 2);
  }

  @Override
  public Region asNode() {
    return gridPane;
  }

  public String getTitle() {
    return tabTitleField.getText();
  }

  public boolean isCanvasTilesTab() {
    return canvasTileTabButton.isSelected();
  }
}

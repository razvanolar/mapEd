package mapEditor.application.main_part.manage_images.manage_tile_sets.cropped_tiles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public class CroppedTilesPathView implements View {

  private GridPane mainContainer = new GridPane();
  private TextField pathTextField;
  private Button pathButton;
  private CheckBox useForAllCheckBox;

  public CroppedTilesPathView() {
    initGUI();
  }

  private void initGUI() {
    pathTextField = new TextField();
    pathButton = new Button("...");
    useForAllCheckBox = new CheckBox("Use for all");
    mainContainer = new GridPane();

    pathTextField.setPrefWidth(350);

    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setHgap(5);
    mainContainer.add(new Text("Path: "), 0, 0);
    mainContainer.add(pathTextField, 1, 0);
    mainContainer.add(pathButton, 2, 0);
    mainContainer.add(useForAllCheckBox, 3, 0);
    mainContainer.setPadding(new Insets(3));

    mainContainer.getStyleClass().add(CssConstants.CROPPED_TILES_PATH_VIEW);
  }

  public TextField getPathTextField() {
    return pathTextField;
  }

  public Button getPathButton() {
    return pathButton;
  }

  public CheckBox getUseForAllCheckBox() {
    return useForAllCheckBox;
  }

  public String getSelectedPath() {
    return pathTextField.getText();
  }

  public boolean usePathForAllTiles() {
    return !useForAllCheckBox.isDisable() && useForAllCheckBox.isSelected();
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}

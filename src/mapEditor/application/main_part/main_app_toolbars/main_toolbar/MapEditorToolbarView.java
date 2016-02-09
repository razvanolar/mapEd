package mapEditor.application.main_part.main_app_toolbars.main_toolbar;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class MapEditorToolbarView implements MapEditorToolbarController.IMapEditorToolbarView {

  private ToolBar toolBar;
  private Button newMapButton;
  private ToggleButton mapEditorViewButton;
  private ToggleButton imageEditorViewButton;

  public MapEditorToolbarView() {
    initGUI();
  }

  private void initGUI() {
    newMapButton = new Button("New Map");
    mapEditorViewButton = new ToggleButton("Map Editor");
    imageEditorViewButton = new ToggleButton("Image Editor");
    toolBar = new ToolBar();

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    spacer.setMinWidth(Region.USE_PREF_SIZE);

    mapEditorViewButton.setSelected(true);
//    imageEditorViewButton.setSelected(true);
    ToggleGroup group = new ToggleGroup();
    group.getToggles().addAll(mapEditorViewButton, imageEditorViewButton);

    toolBar.getItems().addAll(newMapButton, spacer, mapEditorViewButton, imageEditorViewButton);
  }

  public ToggleButton getMapEditorViewButton() {
    return mapEditorViewButton;
  }

  public ToggleButton getImageEditorViewButton() {
    return imageEditorViewButton;
  }

  @Override
  public Region asNode() {
    return toolBar;
  }
}

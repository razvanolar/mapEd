package mapEditor.application.main_part.main_app_toolbars.main_toolbar;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.app_utils.views.others.FillToolItem;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class MapEditorToolbarView implements MapEditorToolbarController.IMapEditorToolbarView {

  private ToolBar toolBar;
  private Button newMapButton;
  private ToggleButton mapEditorViewButton;
  private ToggleButton imageEditorViewButton;
  private ToggleGroup toggleGroup;

  public MapEditorToolbarView() {
    initGUI();
  }

  private void initGUI() {
    newMapButton = new Button("New Map");
    mapEditorViewButton = new ToggleButton("Map Editor");
    imageEditorViewButton = new ToggleButton("Image Editor");
    toolBar = new ToolBar();

    mapEditorViewButton.setSelected(true);
//    imageEditorViewButton.setSelected(true);
    toggleGroup = new ToggleGroup();
    toggleGroup.getToggles().addAll(mapEditorViewButton, imageEditorViewButton);

    toolBar.getItems().addAll(newMapButton, new FillToolItem(), mapEditorViewButton, imageEditorViewButton);
  }

  public Button getNewMapButton() {
    return newMapButton;
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

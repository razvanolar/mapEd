package mapEditor.application.main_part.main_app_toolbars.main_toolbar;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.app_utils.views.others.FillToolItem;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class MapEditorToolbarView implements MapEditorToolbarController.IMapEditorToolbarView {

  private ToolBar toolBar;
  private Button newMapButton;
  private ToggleButton change2DVisibility;
  private ToggleButton changeGridVisibility;
  private ToggleButton showGridButton;
  private ToggleButton mapEditorViewButton;
  private ToggleButton imageEditorViewButton;

  public MapEditorToolbarView() {
    initGUI();
  }

  private void initGUI() {
    newMapButton = new Button("New Map");
    change2DVisibility = new ToggleButton("2D Visibility");
    changeGridVisibility = new ToggleButton("Grid Visibility");
    showGridButton = new ToggleButton("Show Grid");
    mapEditorViewButton = new ToggleButton("Map Editor");
    imageEditorViewButton = new ToggleButton("Image Editor");
    toolBar = new ToolBar();

    mapEditorViewButton.setSelected(true);
//    imageEditorViewButton.setSelected(true);
    ToggleGroup editorsGroup = new ToggleGroup();
    ToggleGroup visibilityGroup = new ToggleGroup();
    editorsGroup.getToggles().addAll(mapEditorViewButton, imageEditorViewButton);
    visibilityGroup.getToggles().addAll(change2DVisibility, changeGridVisibility);

    toolBar.getItems().addAll(newMapButton,
            new Separator(Orientation.HORIZONTAL),
            change2DVisibility,
            changeGridVisibility,
            new Separator(Orientation.HORIZONTAL),
            showGridButton,
            new FillToolItem(),
            mapEditorViewButton,
            imageEditorViewButton);
  }

  public Button getNewMapButton() {
    return newMapButton;
  }

  public ToggleButton getChange2DVisibility() {
    return change2DVisibility;
  }

  public ToggleButton getChangeGridVisibility() {
    return changeGridVisibility;
  }

  public ToggleButton getShowGridButton() {
    return showGridButton;
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

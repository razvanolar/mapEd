package mapEditor.application.main_part.manage_images.manage_tile_sets;

import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.views.others.FillToolItem;
import mapEditor.application.main_part.manage_images.manage_tile_sets.configurations.ManageConfigurationController;
import mapEditor.application.main_part.manage_images.manage_tile_sets.configurations.ManageConfigurationView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.TabContentView;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ManageTileSetsView implements ManageTileSetsController.IManageTileSetsView {

  private SplitPane mainSplitPane;
  private TabPane tabPane;
  private Button addNewTabButton;
  private Button removeTabButton;
  private Button saveCroppedTilesButton;
  private Button settingsButton;
  private Button cropSelectionButton;
  private Button saveTileSetButton;
  private Button resetConfigurationButton;
  private ManageConfigurationController.IManageConfigurationView manageConfigurationView;

  private ToolBar tabsToolbar;
  private ToolBar verticalToolBar;
  private ToggleButton simpleViewButton;

  public ManageTileSetsView() {
    initGUI();
  }

  private void initGUI() {
    addNewTabButton = new Button("Add Tab");
    removeTabButton = new Button("Remove Tab");
    saveCroppedTilesButton = new Button("Save Tiles");
    settingsButton = new Button("Settings");
    cropSelectionButton = new Button("Crop Selection");
    saveTileSetButton = new Button("Save Tile Set");
    resetConfigurationButton = new Button("Reset");
    manageConfigurationView = new ManageConfigurationView();
    tabPane = new TabPane();
    tabsToolbar = new ToolBar();
    ToolBar configurationToolbar = new ToolBar();
    ScrollPane rightScrollPane = new ScrollPane(manageConfigurationView.asNode());
    BorderPane rightPane = new BorderPane(rightScrollPane);
    mainSplitPane = new SplitPane(tabPane, rightPane);
    Button upButton = new Button("Up");
    Button downButton = new Button("Down");
    Button clearButton = new Button("Clear");
    simpleViewButton = new ToggleButton("Simple");
    verticalToolBar = new ToolBar(new Group(upButton),
            new Group(downButton),
            new Group(clearButton),
            new Group(simpleViewButton));
    verticalToolBar.setOrientation(Orientation.VERTICAL);

    upButton.setRotate(-90);
    downButton.setRotate(-90);
    clearButton.setRotate(-90);
    simpleViewButton.setRotate(-90);

    tabsToolbar.getItems().addAll(addNewTabButton, removeTabButton, saveCroppedTilesButton,
            new FillToolItem(), cropSelectionButton, saveTileSetButton);
    configurationToolbar.getItems().addAll(resetConfigurationButton, settingsButton);

    rightPane.setBottom(configurationToolbar);

    rightScrollPane.getStyleClass().add(CssConstants.SCROLL_PANE_BG);
    rightScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    rightScrollPane.setMinWidth(300);
    manageConfigurationView.asNode().prefWidthProperty().bind(rightScrollPane.widthProperty());

    SplitPane.setResizableWithParent(rightPane, false);
    mainSplitPane.setOrientation(Orientation.HORIZONTAL);
    mainSplitPane.setDividerPositions(0.8);
  }

  public void setState(ManageTileSetsController.IManageConfigurationViewState state) {
    switch (state) {
      case NO_TAB_SELECTED:
        cropSelectionButton.setDisable(true);
        break;
      case NO_IMAGE_SELECTED:
        cropSelectionButton.setDisable(true);
        break;
      case FULL_SELECTION:
        cropSelectionButton.setDisable(false);
        break;
    }
  }

  public ScrollPane addTab(String title, TabContentView content) {
    Tab tab = new Tab(title, content.asNode());
    tab.setClosable(true);
    tab.setUserData(content);
    tabPane.getTabs().add(tab);
    tabPane.getSelectionModel().select(tab);

    return content.getCanvasContainer();
  }

  public boolean isSimpleView() {
    return simpleViewButton.isSelected();
  }

  public TabPane getTabPane() {
    return tabPane;
  }

  public Button getAddNewTabButton() {
    return addNewTabButton;
  }

  public Button getRemoveTabButton() {
    return removeTabButton;
  }

  public Button getSaveCroppedTilesButton() {
    return saveCroppedTilesButton;
  }

  public Button getCropSelectionButton() {
    return cropSelectionButton;
  }

  public Button getSettingsButton() {
    return settingsButton;
  }

  public Button getSaveTileSetButton() {
    return saveTileSetButton;
  }

  public Button getResetConfigurationButton() {
    return resetConfigurationButton;
  }

  public ToolBar getTabsToolbar() {
    return tabsToolbar;
  }

  public ToolBar getVerticalToolBar() {
    return verticalToolBar;
  }

  public ToggleButton getSimpleViewButton() {
    return simpleViewButton;
  }

  public ManageConfigurationController.IManageConfigurationView getManageConfigurationView() {
    return manageConfigurationView;
  }

  @Override
  public Region asNode() {
    return mainSplitPane;
  }
}

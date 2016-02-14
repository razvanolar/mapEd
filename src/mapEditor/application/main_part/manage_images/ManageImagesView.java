package mapEditor.application.main_part.manage_images;

import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.views.others.FillToolItem;
import mapEditor.application.main_part.manage_images.configurations.ManageConfigurationController;
import mapEditor.application.main_part.manage_images.configurations.ManageConfigurationView;
import mapEditor.application.main_part.manage_images.cropped_tiles.CroppedTilesPathView;
import mapEditor.application.main_part.manage_images.utils.TabContentView;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ManageImagesView implements ManageImagesController.IManageImagesView {

  private SplitPane mainSplitPane;
  private TabPane tabPane;
  private Button addNewTabButton;
  private Button removeTabButton;
  private Button renameTabButton;
  private Button settingsButton;
  private Button cropSelectionButton;
  private Button saveTileSetButton;
  private Button resetConfigurationButton;
  private ManageConfigurationController.IManageConfigurationView manageConfigurationView;

  private ToolBar tabsToolbar;

  public ManageImagesView() {
    initGUI();
  }

  private void initGUI() {
    addNewTabButton = new Button("Add Tab");
    removeTabButton = new Button("Remove Tab");
    renameTabButton = new Button("Rename Tab");
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

    tabsToolbar.getItems().addAll(addNewTabButton, removeTabButton, renameTabButton, settingsButton, new FillToolItem(),
            cropSelectionButton, saveTileSetButton);
    configurationToolbar.getItems().addAll(resetConfigurationButton);

    rightPane.setBottom(configurationToolbar);

    rightScrollPane.getStyleClass().add(CssConstants.SCROLL_PANE_BG);
    rightScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    rightScrollPane.setMinWidth(300);
    manageConfigurationView.asNode().prefWidthProperty().bind(rightScrollPane.widthProperty());

    SplitPane.setResizableWithParent(rightPane, false);
    mainSplitPane.setOrientation(Orientation.HORIZONTAL);
    mainSplitPane.setDividerPositions(0.8);
  }

  public void setState(ManageImagesController.IManageConfigurationViewState state) {
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

  public ScrollPane addTab(String title, Canvas canvas, CroppedTilesPathView pathView) {
    TabContentView content = new TabContentView(canvas, pathView);

    Tab tab = new Tab(title, content.asNode());
    tab.setClosable(true);
    tab.setUserData(content);
    tabPane.getTabs().add(tab);
    tabPane.getSelectionModel().select(tab);

    return content.getCanvasContainer();
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

  public Button getRenameTabButton() {
    return renameTabButton;
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

  public ManageConfigurationController.IManageConfigurationView getManageConfigurationView() {
    return manageConfigurationView;
  }

  @Override
  public Region asNode() {
    return mainSplitPane;
  }
}

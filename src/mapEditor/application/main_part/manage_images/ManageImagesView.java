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
  private Button saveTileSetButton;
  private Button resetConfigurationButton;
  private ManageConfigurationController.IManageConfigurationView manageConfigurationView;

  public ManageImagesView() {
    initGUI();
  }

  private void initGUI() {
    addNewTabButton = new Button("Add Tab");
    removeTabButton = new Button("Remove Tab");
    renameTabButton = new Button("Rename Tab");
    settingsButton = new Button("Settings");
    saveTileSetButton = new Button("Save Tile Set");
    resetConfigurationButton = new Button("Reset");
    manageConfigurationView = new ManageConfigurationView();
    tabPane = new TabPane();
    ToolBar tabsToolbar = new ToolBar();
    ToolBar configurationToolbar = new ToolBar();
    BorderPane tabsContainer = new BorderPane();
    BorderPane configurationPanel = new BorderPane();
    SplitPane canvasSplitPane = new SplitPane(tabsContainer, configurationPanel);
    ScrollPane leftScrollPane = new ScrollPane(manageConfigurationView.asNode());
    BorderPane leftPane = new BorderPane(leftScrollPane);
    mainSplitPane = new SplitPane(canvasSplitPane, leftPane);

    tabsToolbar.getItems().addAll(addNewTabButton, removeTabButton, renameTabButton, settingsButton, new FillToolItem(),
            saveTileSetButton);
    configurationToolbar.getItems().addAll(resetConfigurationButton);

    leftPane.setBottom(configurationToolbar);

    tabsContainer.setCenter(tabPane);
    tabsContainer.setBottom(tabsToolbar);

    leftScrollPane.getStyleClass().add(CssConstants.SCROLL_PANE_BG);
    leftScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    leftScrollPane.setMinWidth(300);
    manageConfigurationView.asNode().prefWidthProperty().bind(leftScrollPane.widthProperty());

    SplitPane.setResizableWithParent(leftPane, false);
    SplitPane.setResizableWithParent(configurationPanel, false);
    mainSplitPane.setOrientation(Orientation.HORIZONTAL);
    mainSplitPane.setDividerPositions(0.8);
    canvasSplitPane.setOrientation(Orientation.VERTICAL);
    canvasSplitPane.setDividerPositions(0.5);
  }

  public ScrollPane addTab(String title, Canvas canvas) {
    ScrollPane pane = new ScrollPane(canvas);

    Tab tab = new Tab(title, pane);
    tab.setClosable(true);
    tab.setUserData(canvas);
    tabPane.getTabs().add(tab);
    tabPane.getSelectionModel().select(tab);

    pane.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);
    return pane;
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

  public Button getSettingsButton() {
    return settingsButton;
  }

  public Button getSaveTileSetButton() {
    return saveTileSetButton;
  }

  public Button getResetConfigurationButton() {
    return resetConfigurationButton;
  }

  public ManageConfigurationController.IManageConfigurationView getManageConfigurationView() {
    return manageConfigurationView;
  }

  @Override
  public Region asNode() {
    return mainSplitPane;
  }
}

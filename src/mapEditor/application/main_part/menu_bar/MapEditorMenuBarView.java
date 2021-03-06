package mapEditor.application.main_part.menu_bar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.Region;

/**
 *
 * Created by razvanolar on 01.02.2016.
 */
public class MapEditorMenuBarView implements MapEditorMenuBarController.IMapEditorMenuBarView {

  private MenuBar menuBar;
  private Menu fileMenu;
  private MenuItem newMenuItem;
  private MenuItem openMenuItem;
  private MenuItem closeProjectMenuItem;
  private MenuItem settingsMenuItem;
  private MenuItem exitMenuItem;
  private Menu editMenu;
  private Menu preferencesMenu;
  private Menu helpMenu;

  public MapEditorMenuBarView() {
    initGUI();
  }

  private void initGUI() {
    newMenuItem = new MenuItem("New");
    openMenuItem = new MenuItem("Open...");
    closeProjectMenuItem = new MenuItem("Close Project");
    settingsMenuItem = new MenuItem("Settings");
    exitMenuItem = new MenuItem("Exit");
    fileMenu = new Menu("File", null, newMenuItem,
            openMenuItem,
            closeProjectMenuItem,
            new SeparatorMenuItem(),
            settingsMenuItem,
            new SeparatorMenuItem(),
            new SeparatorMenuItem(),
            exitMenuItem);
    editMenu = new Menu("Edit");
    preferencesMenu = new Menu("Preferences");
    helpMenu = new Menu("Help");
    menuBar = new MenuBar(fileMenu, editMenu, preferencesMenu, helpMenu);
  }

  public MenuItem getCloseProjectMenuItem() {
    return closeProjectMenuItem;
  }

  public MenuItem getExitMenuItem() {
    return exitMenuItem;
  }

  @Override
  public Region asNode() {
    return menuBar;
  }
}

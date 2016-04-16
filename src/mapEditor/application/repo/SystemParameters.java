package mapEditor.application.repo;

import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.models.MessageKey;
import mapEditor.application.repo.models.LWProjectModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class SystemParameters {

  public static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
  public static final String CONFIG_FILE_PATH = "med_config\\";
  public static final String KNOWN_PROJECTS_FILE_PATH = CONFIG_FILE_PATH + "projects\\";
  public static final String RESOURCES_FILE_PATH = "resources\\";
  public static final String TEMPLATES_FILE_PATH = RESOURCES_FILE_PATH + "templates\\";
  public static final String HTML_EXPORTER_TEMPLATES_FILE_PATH = TEMPLATES_FILE_PATH + "html_exporter\\";
  public static final String PROJECT_FILE_EXT = ".med";
  public static final String MAP_FILE_EXT = ".map";
  public static final String TILE_GROUPS_FOLDER_PATH = "tile_groups\\";
  public static final String TILES_FOLDER_PATH = TILE_GROUPS_FOLDER_PATH + "tiles\\";
  public static final String TILE_SETS_FOLDER_PATH = TILE_GROUPS_FOLDER_PATH + "tile_sets\\";
  public static final String BRUSHES_FOLDER_PATH = TILE_GROUPS_FOLDER_PATH + "brushes\\";
  public static final String CHARACTERS_FOLDER_PATH = "characters\\";
  public static final String MAPS_FOLDER_PATH = "maps\\";
  public static final String UNTITLED_TAB_NAME = "*Untitled";
  public static final String UNTITLED_MAP_TAB = UNTITLED_TAB_NAME + MAP_FILE_EXT;

  /**
   * Constants for exporting map to html
   */
  public static final String PRELOAD_IMAGES_FUNCTION_NAME = "preload";
  public static final String PRELOAD_IMAGES_FILE_NAME = "preload_images.js";
  public static final String ATTRIBUTES_FILE_NAME = "attributes.js";
  public static final String EXPORTED_TILES_FOLDER_PATH = "tiles\\";
  public static final String JAVA_SCRIPT_NULL_VALUE = "null";

  public static final int MAP_MIN_SIZE_NUMBER = 1;
  public static final int MAP_MAX_SIZE_NUMBER = 9999;
  public static final int MAP_DEFAULT_SIZE_NUMBER = 40;

  public static final Color MAP_DEFAULT_BG_COLOR = new Color(.5, .5, .5, .5);
  public static final Color MAP_DEFAULT_GRID_COLOR = Color.DARKCYAN;
  public static final Color MAP_DEFAULT_SQUARE_COLOR = Color.YELLOW;
  public static final Color EDIT_SELECTABLE_TILE_HOVER_COLOR = new Color(.3, .5, .5, .7);

  public static List<LWProjectModel> PROJECTS;

  public static final MessageKey MESSAGE_KEY = new MessageKey();

  public static final boolean DEFAULT_SHOW_GRID_VALUE = true;
  public static final boolean DEFAULT_SHOW_PROJECT_TREE_VALUE = true;

  public static final int PRIMARY_BRUSH_ROWS = 3;
  public static final int PRIMARY_BRUSH_COLUMNS = 3;
  public static final int SECONDARY_BRUSH_ROWS = 2;
  public static final int SECONDARY_BRUSH_COLUMNS = 2;

  public static List<Thread> watchers = new ArrayList<>();
  public static Thread appListenerThread;
}

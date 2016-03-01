package mapEditor.application.main_part.app_utils;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import mapEditor.application.repo.models.ProjectModel;

/**
 *
 * Created by razvanolar on 03.02.2016.
 */
public class AppParameters {

  public static ProjectModel CURRENT_PROJECT;

  public static Color HOVERED_LAYER_COLOR = Color.LAVENDER;
  public static Color SELECTED_LAYER_COLOR = Color.CORNFLOWERBLUE;
  public static Color HOVERED_DETAILED_TILE_COLOR = Color.LAVENDER;
  public static Color SELECTED_DETAILED_TILE_COLOR = Color.CORNFLOWERBLUE;
  public static Background HOVERED_LAYER_BG = new Background(new BackgroundFill(HOVERED_LAYER_COLOR, null, null));
  public static Background SELECTED_LAYER_BG = new Background(new BackgroundFill(SELECTED_LAYER_COLOR, null, null));
  public static Background HOVERED_DETAILED_TILE_BG = new Background(new BackgroundFill(HOVERED_DETAILED_TILE_COLOR, null, null));
  public static Background SELECTED_DETAILED_TILE_BG = new Background(new BackgroundFill(SELECTED_DETAILED_TILE_COLOR, null, null));
  public static final Background TRANSPARENT_BG = new Background(new BackgroundFill(Color.TRANSPARENT, null, null));

  public static String DEFAULT_PROJECT_NAME = "Unnamed";
  public static String SYSTEM_FILES_VIEW_PATH = "C:\\Users\\razvanolar\\Desktop\\MapEditor_Util\\projects";

  public static int DEFAULT_CELL_SIZE = 32;
}

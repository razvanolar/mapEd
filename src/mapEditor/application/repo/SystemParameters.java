package mapEditor.application.repo;

import mapEditor.application.repo.models.LWProjectModel;

import java.util.List;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class SystemParameters {

  public static String CONFIG_FILE_PATH = "med_config\\";
  public static String KNOWN_PROJECTS_FILE_PATH = CONFIG_FILE_PATH + "projects\\";
  public static String PROJECT_FILE_EXT = ".med";
  public static String TILE_GROUPS_FOLDER_PATH = "tile_groups\\";
  public static String CHARACTERS_FOLDER_PATH = "characters\\";
  public static String MAPS_FOLDER_PATH = "maps\\";

  public static List<LWProjectModel> PROJECTS;
}

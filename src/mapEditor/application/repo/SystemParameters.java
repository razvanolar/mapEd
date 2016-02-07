package mapEditor.application.repo;

import mapEditor.application.repo.models.LWProjectModel;

import java.util.List;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class SystemParameters {

  public static String CONFIG_FILE_PATH = "med_config\\";
  public static String KNOWN_PROJECTS_FILE_PATH = CONFIG_FILE_PATH + "projects";

  public static List<LWProjectModel> PROJECTS;
}

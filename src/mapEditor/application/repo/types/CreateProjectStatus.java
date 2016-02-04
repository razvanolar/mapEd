package mapEditor.application.repo.types;

/**
 *
 * Created by razvanolar on 04.02.2016.
 */
public enum  CreateProjectStatus {
  /**
   * VALID - if the name of the project and the specified path are valid
   * PATH_MISSING - if the specified project path does not exist
   * NAME_EXISTS - if the path is valid but at that location already exist another file with the specified name
   */
  VALID, PATH_MISSING, NAME_EXISTS
}

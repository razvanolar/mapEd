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
   * ANOTHER_CREATED - if at the specified location is already a .med file created
   * NOT_DIRECTORY - if the specified path exist but  is not directory
   */
  VALID, PATH_MISSING, NAME_EXISTS, ANOTHER_CREATED, NOT_DIRECTORY
}

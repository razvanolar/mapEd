package mapEditor.application.main_part.app_utils.inputs;

import mapEditor.application.main_part.app_utils.AppParameters;

/**
 *
 * Created by razvanolar on 23.01.2016.
 */
public class StringValidator {

  public static boolean isNullOrEmpty(String value) {
    return value == null || value.isEmpty();
  }

  /**
   * Checks to see if the value represents a valid file name.
   * Means that value must not be empty, it must start with a letter and the rest of the characters are
   *  a combination of letters and numbers.
   * @param value
   * String value
   * @return true - if value it's a valid file name; false otherwise
   */
  public static boolean isValidFileName(String value) {
    return !isNullOrEmpty(value) && value.matches("^[a-zA-Z][\\w]*");
  }

  /**
   * Checks to see if the provided path is a valid tiles path.
   * A valid tiles path is the project tiles directory and any of its sub-directories.
   * So, the path parameter must contain the project tiles path to be valid.
   * @param path
   * directory path
   * @return true if its a valid tiles path; false otherwise
   */
  public static boolean isValidTilesPath(String path) {
    return !isNullOrEmpty(path) && path.contains(AppParameters.CURRENT_PROJECT.getTilesFile().getAbsolutePath());
  }

  /**
   * Checks to see if the provided path is a valid brushes path.
   * A valid brushes path is the project tiles directory and any of its sub-directories.
   * So, the path parameter must contain the project brushes path to be valid.
   * @param path
   * directory path
   * @return true if its a valid brushes path; false otherwise
   */
  public static boolean isValidBrushesPath(String path) {
    return !isNullOrEmpty(path) && path.contains(AppParameters.CURRENT_PROJECT.getBrushesFile().getAbsolutePath());
  }
}

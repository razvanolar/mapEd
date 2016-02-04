package mapEditor.application.main_part.app_utils.inputs;

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
   * @param value - String value
   * @return true - if value it's a valid file name; false otherwise
   */
  public static boolean isValidFileName(String value) {
    return !isNullOrEmpty(value) && value.matches("^[a-zA-Z][\\w]*");
  }
}

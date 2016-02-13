package mapEditor.application.main_part.app_utils.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by razvanolar on 03.02.2016.
 */
public enum KnownFileExtensions {
  UNKNOWN(""), PNG(".png"), JPG(".jpg");


  String extension;
  KnownFileExtensions(String extension) {
    this.extension = extension;
  }

  public String getExtension() {
    return extension;
  }

  public String forRegex() {
    return "\\" + extension;
  }

  public static List<KnownFileExtensions> getExtensions() {
    ArrayList<KnownFileExtensions> result = new ArrayList<>(Arrays.asList(values()));
    result.remove(UNKNOWN);
    return result;
  }
}

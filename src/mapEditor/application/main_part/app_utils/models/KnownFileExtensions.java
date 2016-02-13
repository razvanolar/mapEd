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
  static List<KnownFileExtensions> allExtensions;
  static List<KnownFileExtensions> imageExtensions;
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
    if (allExtensions == null) {
      allExtensions = new ArrayList<>(Arrays.asList(values()));
      allExtensions.remove(UNKNOWN);
    }
    return allExtensions;
  }

  public static List<KnownFileExtensions> getImageExtensions() {
    if (imageExtensions == null) {
      imageExtensions = new ArrayList<>();
      imageExtensions.add(PNG);
      imageExtensions.add(JPG);
    }
    return imageExtensions;
  }
}

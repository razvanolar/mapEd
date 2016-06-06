package mapEditor.application.main_part.app_utils.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by razvanolar on 03.02.2016.
 */
public enum KnownFileExtensions {
  NONE("", ""),
  UNKNOWN("", ""),
  PNG(".png", "png"),
  JPG(".jpg", "jpg"),
  MAP(".map", "map"),
  BRUSH(".brush", "brush"),
  OBJECT(".object", "object"),
  TMX(".tmx", "tmx");


  String extension;
  String format;
  static List<KnownFileExtensions> allExtensions;
  static List<KnownFileExtensions> imageExtensions;
  KnownFileExtensions(String extension, String format) {
    this.extension = extension;
    this.format = format;
  }

  public String getExtension() {
    return extension;
  }

  public String getFormat() {
    return format;
  }

  public String forRegex() {
    return "\\" + extension;
  }

  public static List<KnownFileExtensions> getExtensions() {
    if (allExtensions == null) {
      allExtensions = new ArrayList<>(Arrays.asList(values()));
      allExtensions.remove(UNKNOWN);
      allExtensions.remove(NONE);
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

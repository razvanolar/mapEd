package mapEditor.application.main_part.app_utils.inputs;

import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;
import mapEditor.application.main_part.app_utils.models.TreeItemType;

import java.util.List;

/**
 *
 * Created by razvanolar on 03.02.2016.
 */
public class FileExtensionUtil {

  /**
   * Gets the extension for the specified name.
   * @param name
   * File name.
   * @return If the extension of the file is recognized as a system extension, it will be returned.
   *         If the extension is not in the system values, UNKNOWN will be returned, otherwise NONE
   *
   * i.e. name.txt - UNKNOWN
   *      .name
   *      name     - NONE
   */
  public static KnownFileExtensions getFileExtension(String name) {
    List<KnownFileExtensions> extensions = KnownFileExtensions.getExtensions();
    for (KnownFileExtensions extension : extensions) {
      if (name.endsWith(extension.getExtension()))
        return extension;
    }
    return hasExtension(name) ? KnownFileExtensions.UNKNOWN : KnownFileExtensions.NONE;
  }

  public static boolean isImageFile(String name) {
    if (StringValidator.isNullOrEmpty(name))
      return false;
    List<KnownFileExtensions> extensions = KnownFileExtensions.getImageExtensions();
    KnownFileExtensions ext = getFileExtension(name);
    return extensions.contains(ext);
  }

  public static boolean isImageExtension(String ext) {
    if (StringValidator.isNullOrEmpty(ext) || !ext.startsWith("."))
      return false;
    List<KnownFileExtensions> extensions = KnownFileExtensions.getImageExtensions();
    for (KnownFileExtensions extension : extensions) {
      if (extension.getExtension().equalsIgnoreCase(ext))
        return true;
    }
    return false;
  }

  public static boolean isImageExtension(KnownFileExtensions ext) {
    if (ext == null)
      return false;
    List<KnownFileExtensions> extensions = KnownFileExtensions.getImageExtensions();
    return extensions.contains(ext);
  }

  public static boolean isPngFile(String name) {
    return !(name == null || getFileExtension(name) != KnownFileExtensions.PNG);
  }

  public static boolean isMapFile(String name) {
    return !StringValidator.isNullOrEmpty(name) && getFileExtension(name) == KnownFileExtensions.MAP;
  }

  public static boolean isBrushFile(String name) {
    return !StringValidator.isNullOrEmpty(name) && getFileExtension(name) == KnownFileExtensions.BRUSH;
  }

  public static boolean isObjectFile(String name) {
    return !StringValidator.isNullOrEmpty(name) && getFileExtension(name) == KnownFileExtensions.OBJECT;
  }

  public static boolean isCharacterFile(String name) {
    return !StringValidator.isNullOrEmpty(name) && getFileExtension(name) == KnownFileExtensions.CHARACTER;
  }

  public static boolean isTmxFile(String name) {
    return !StringValidator.isNullOrEmpty(name) && getFileExtension(name) == KnownFileExtensions.TMX;
  }

  /**
   * Returns the name of the specified file name without containing it's extension.
   * @param fileName
   * File name.
   * @return The file name without extension.
   *         NULL if fileName is null.
   */
  public static String getFileNameWithoutExtension(String fileName) {
    if (fileName == null)
      return null;
    StringBuilder builder = new StringBuilder(fileName);
    int index = builder.lastIndexOf(".");
    if (index == -1)
      return fileName;
    return builder.substring(0, index);
  }

  /**
   * Remove the specified extension from the specified name.
   * @param name file name
   * @return String
   */
  public static String getNameWithoutExtension(String name, KnownFileExtensions ext) {
    if (!name.endsWith(ext.getExtension()))
      return name;
    return getFileNameWithoutExtension(name);
  }

  /**
   * Add the specified extension to the specified name, if it does not have it.
   * @param name String
   * @return file name
   */
  public static String getNameWithExtension(String name, KnownFileExtensions ext) {
    if (name.endsWith(ext.getExtension()))
      return name;
    return name + ext.getExtension();
  }

  public static boolean hasExtension(String name) {
    return !StringValidator.isNullOrEmpty(name) && !StringValidator.isNullOrEmpty(getStringExtension(name));
  }

  public static TreeItemType getTreeItemTypeForName(String name) {
    if (isImageFile(name))
      return TreeItemType.IMAGE;
    if (isMapFile(name))
      return TreeItemType.MAP;
    return TreeItemType.NORMAL;
  }

  private static String getStringExtension(String name) {
    StringBuilder builder = new StringBuilder(name);
    int index = builder.lastIndexOf(".");
    return index == -1 ? null : builder.substring(index);
  }
}
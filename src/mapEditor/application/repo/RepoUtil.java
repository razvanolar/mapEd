package mapEditor.application.repo;

import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;

import java.io.File;

/**
 *
 * Created by razvanolar on 11.02.2016.
 */
public class RepoUtil {

  private static final String ORDER_REGEX = "[(][0-9]+[)]";
  private static final String ORDERED_FILE_REGEX = ".*" + ORDER_REGEX + ".*";

  /**
   * Checks to see if in the specified directory are already files named like fileName parameter.
   * If there are such files, it computes a new name with a new order file number.
   *
   * Assume in the directory we have the following files:
   *    file.txt
   *    file(1).txt
   * The returned file name will be: file(2).txt
   *
   * @param directory
   * Valid directory path.
   * @param fileName
   * File name that needs an order number to be assigned to it.
   * @return The file name with next order number
   */
  public String getAlternativeNameForExistingFile(String directory, String fileName) {
    if (directory == null || fileName == null)
      return null;
    directory = directory.endsWith("\\") ? directory : directory + "\\";
    File file = new File(directory + fileName);
    if (!file.exists())
      return null;

    String extension = getFileExtension(fileName);
    String nameWithoutOrder = getFileNameWithoutOrder(fileName);
    if (extension == null || nameWithoutOrder == null)
      return null;

    File[] files = new File(directory).listFiles();
    if (files == null)
      return null;
    if (files.length == 0)
      return fileName;
    int k = 0;
    for (File f : files) {
      String fName = f.getName();
      if (fName.matches(fileName) ||
              fName.matches(nameWithoutOrder + extension) ||
              fName.matches(nameWithoutOrder + ORDER_REGEX + extension)) {
        k++;
      }
    }
    if (k == 0)
      return fileName;
    return nameWithoutOrder + "(" + k + ")" + extension;
  }

  /**
   * If fileName is not in specified directory, it will be returned as it is.
   * Otherwise, a file name with an associated order number will be returned.
   * @param directory
   * Valid directory path.
   * @param fileName
   * File name that needs an order number to be assigned to it.
   * @return The file name with next order number
   */
  public String checkNameOrGetAnAlternativeOne(String directory, String fileName) {
    if (!directory.endsWith("\\"))
      directory += "\\";
    File file = new File(directory + fileName);
    if (file.exists())
      return getAlternativeNameForExistingFile(directory, fileName);
    return fileName;
  }

  public String checkBrushNameOrGetANewOne(String directory, String fileName) {
    if (fileName == null)
      return null;
    fileName = fileName.endsWith(KnownFileExtensions.BRUSH.getExtension()) ? fileName : fileName + KnownFileExtensions.BRUSH.getExtension();
    return checkNameOrGetAnAlternativeOne(directory, fileName);
  }

  public String checkObjectNameOrGetANewOne(String directory, String fileName) {
    if (fileName == null)
      return null;
    fileName = fileName.endsWith(KnownFileExtensions.OBJECT.getExtension()) ? fileName : fileName + KnownFileExtensions.OBJECT.getExtension();
    return checkNameOrGetAnAlternativeOne(directory, fileName);
  }

  public String checkCharacterNameOrGetANewOne(String directory, String fileName) {
    if (fileName == null)
      return null;
    fileName = fileName.endsWith(KnownFileExtensions.CHARACTER.getExtension()) ? fileName : fileName + KnownFileExtensions.CHARACTER.getExtension();
    return checkNameOrGetAnAlternativeOne(directory, fileName);
  }

  /**
   * Returns the file extension.
   * @param fileName
   * File name.
   * @return The file extension but without '.'
   *         An empty string if the extension is missing.
   *         NULL if specified file name is null
   */
  public String getFileExtensionWithoutDot(String fileName) {
    String ext = getFileExtension(fileName);
    if (ext == null)
      return null;
    if (ext.length() == 0)
      return "";
    StringBuilder builder = new StringBuilder(ext);
    int index = builder.lastIndexOf(".");
    if (index == -1)
      return ext;
    return builder.substring(index + 1, ext.length());
  }

  /**
   * Returns the file extension.
   * @param fileName
   * File name.
   * @return The file extension.
   *         An empty string if the extension is missing.
   *         NULL if specified file name is null.
   */
  public String getFileExtension(String fileName) {
    if (fileName == null)
      return null;
    StringBuilder builder = new StringBuilder(fileName);
    int index = builder.lastIndexOf(".");
    if (index == -1)
      return "";
    return builder.substring(index, fileName.length());
  }

  /**
   * Returns the name of the specified file without containing the order number or it's extension.
   * @param fileName
   * File name.
   * @return The file name.
   *         NULL if fileName is null.
   */
  public String getFileNameWithoutOrder(String fileName) {
    String name = FileExtensionUtil.getFileNameWithoutExtension(fileName);
    if (name == null)
      return null;
    if (!name.matches(ORDERED_FILE_REGEX))
      return name;
    StringBuilder builder = new StringBuilder(name);
    int index = builder.lastIndexOf("(");
    return builder.substring(0, index);
  }
}

package mapEditor.application.repo;

import java.io.File;

/**
 *
 * Created by razvanolar on 11.02.2016.
 */
public class RepoUtil {

  public String getAlternativeNameForExistingFile(String directory, String fileName) {
    if (directory == null || fileName == null)
      return null;
    directory = directory.endsWith("\\") ? directory : directory + "\\";
    File file = new File(directory + fileName);
    if (!file.exists())
      return null;

    String extension = getFileExtension(fileName);
    String name = getFileNameWithoutExtension(fileName);
    if (extension == null || name == null)
      return null;

    File[] files = new File(directory).listFiles();
    if (files == null)
      return null;
    if (files.length == 0)
      return fileName;
    int k = 0;
    for (File f : files) {
      if (f.getName().matches(fileName) || f.getName().matches(name + "[(][0-9]+[)]" + extension))
        k ++;
    }
    if (k == 0)
      return fileName;
    return name + "(" + k + ")" + extension;
  }

  public String checkNameOrGetAnAlternativeOne(String directory, String fileName) {
    if (!directory.endsWith("\\"))
      directory += "\\";
    File file = new File(directory + fileName);
    if (file.exists())
      return getAlternativeNameForExistingFile(directory, fileName);
    return fileName;
  }

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

  public String getFileExtension(String fileName) {
    if (fileName == null)
      return null;
    StringBuilder builder = new StringBuilder(fileName);
    int index = builder.lastIndexOf(".");
    if (index == -1)
      return "";
    return builder.substring(index, fileName.length());
  }

  public String getFileNameWithoutExtension(String fileName) {
    if (fileName == null)
      return null;
    StringBuilder builder = new StringBuilder(fileName);
    int index = builder.lastIndexOf(".");
    if (index == -1)
      return fileName;
    return builder.substring(0, index);
  }
}

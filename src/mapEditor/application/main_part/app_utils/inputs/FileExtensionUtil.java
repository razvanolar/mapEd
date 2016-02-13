package mapEditor.application.main_part.app_utils.inputs;

import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;

import java.util.List;

/**
 *
 * Created by razvanolar on 03.02.2016.
 */
public class FileExtensionUtil {

  public static KnownFileExtensions getFileExtension(String name) {
    List<KnownFileExtensions> extensions = KnownFileExtensions.getExtensions();
    for (KnownFileExtensions extension : extensions) {
      if (name.endsWith(extension.getExtension()))
        return extension;
    }
    return KnownFileExtensions.UNKNOWN;
  }
}

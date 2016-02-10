package mapEditor.application.main_part.app_utils.inputs;

import javafx.scene.image.ImageView;
import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;

/**
 *
 * Created by razvanolar on 03.02.2016.
 */
public class TreeFileNodeGraphicsProvider {

  public static ImageView getImageForExtension(KnownFileExtensions extension) {
    switch (extension) {
      case UNKNOWN:
        return new ImageView(ImageProvider.genericFile());
    }
    return null;
  }
}

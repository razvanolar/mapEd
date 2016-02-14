package mapEditor.application.main_part.manage_images.utils;

import mapEditor.application.main_part.manage_images.cropped_tiles.CroppedTileController;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public interface ManageImagesListener {

  void saveCroppedImage(CroppedTileController.ICroppedTileView view);
  void dropCroppedTileView(CroppedTileController.ICroppedTileView view);
}

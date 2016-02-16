package mapEditor.application.main_part.manage_images.utils;

import mapEditor.application.main_part.manage_images.cropped_tiles.detailed_view.CroppedTilesDetailedController;

/**
 *
 * Created by razvanolar on 14.02.2016.
 */
public interface ManageImagesListener {

  void saveCroppedImage(CroppedTilesDetailedController.ICroppedTileDetailedView view);
  void dropCroppedTileView(CroppedTilesDetailedController.ICroppedTileDetailedView view);
}

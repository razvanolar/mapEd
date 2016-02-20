package mapEditor.application.repo.results;

import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.repo.statuses.SaveImagesStatus;

import java.util.List;

/**
 *
 * Created by razvanolar on 20.02.2016.
 */
public class SaveImagesResult {

  private SaveImagesStatus status;
  private List<ImageModel> unsavedImages;

  public SaveImagesResult(SaveImagesStatus status, List<ImageModel> unsavedImages) {
    this.status = status;
    this.unsavedImages = unsavedImages;
  }

  public SaveImagesStatus getStatus() {
    return status;
  }

  public List<ImageModel> getUnsavedImages() {
    return unsavedImages;
  }
}

package mapEditor.application.repo.results;

import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.repo.statuses.SaveFilesStatus;

import java.util.List;

/**
 *
 * Created by razvanolar on 20.02.2016.
 */
public class SaveImagesResult {

  private SaveFilesStatus status;
  private List<ImageModel> unsavedImages;

  public SaveImagesResult(SaveFilesStatus status, List<ImageModel> unsavedImages) {
    this.status = status;
    this.unsavedImages = unsavedImages;
  }

  public SaveFilesStatus getStatus() {
    return status;
  }

  public List<ImageModel> getUnsavedImages() {
    return unsavedImages;
  }
}

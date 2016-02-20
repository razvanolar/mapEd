package mapEditor.application.repo.statuses;

/**
 * The result status of RepoController saveImages method.
 *
 * Created by razvanolar on 20.02.2016.
 */
public enum  SaveImagesStatus {

  /**
   * No image was saved. This could happen in the following cases:
   *  - images list is null or empty
   *  - no image could be saved
   */
  NONE("No image was saved on the disk !"),

  /**
   * Only a part of the images are saved.
   */
  PARTIAL("Only a part of the images were saved. Please check your tiles directory to see the results."),

  /**
   * All the images were saved successfully.
   */
  COMPLETE("All image were saved successfully !");

  String message;
  SaveImagesStatus(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

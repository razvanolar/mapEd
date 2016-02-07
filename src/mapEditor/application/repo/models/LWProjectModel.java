package mapEditor.application.repo.models;


/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class LWProjectModel {

  private String projectPath;
  private long lastAccessedTime;

  public LWProjectModel(String projectPath, long lastAccessedTime) {
    this.projectPath = projectPath;
    this.lastAccessedTime = lastAccessedTime;
  }

  public String getProjectPath() {
    return projectPath;
  }

  public long getLastAccessedTime() {
    return lastAccessedTime;
  }
}

package mapEditor.application.repo.models;


/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class LWProjectModel {

  private String path;
  private String name;
  private long lastAccessedTime;

  public LWProjectModel(String name, String projectPath, long lastAccessedTime) {
    this.name = name;
    this.path = projectPath;
    this.lastAccessedTime = lastAccessedTime;
  }

  public String getPath() {
    return path;
  }

  public String getName() {
    return name;
  }

  public long getLastAccessedTime() {
    return lastAccessedTime;
  }
}

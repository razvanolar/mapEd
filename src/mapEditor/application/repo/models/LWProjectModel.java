package mapEditor.application.repo.models;


import mapEditor.application.repo.types.ProjectStatus;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class LWProjectModel {

  private String path;
  private String name;
  private long lastAccessedTime;
  private ProjectStatus status;

  public LWProjectModel(String name, String projectPath, long lastAccessedTime, ProjectStatus status) {
    this.name = name;
    this.path = projectPath;
    this.lastAccessedTime = lastAccessedTime;
    this.status = status;
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

  public ProjectStatus getStatus() {
    return status;
  }

  public void setLastAccessedTime(long lastAccessedTime) {
    this.lastAccessedTime = lastAccessedTime;
  }

  public void setStatus(ProjectStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return lastAccessedTime + " : " + name + " " + status;
  }
}

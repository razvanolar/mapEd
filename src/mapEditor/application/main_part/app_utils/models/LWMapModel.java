package mapEditor.application.main_part.app_utils.models;

/**
 *
 * Created by razvanolar on 27.02.2016.
 */
public class LWMapModel {

  private String name;
  private String relativePath;
  private boolean selected;

  public LWMapModel(String name, String relativePath, boolean selected) {
    this.name = name;
    this.relativePath = relativePath;
    this.selected = selected;
  }

  public String getName() {
    return name;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}

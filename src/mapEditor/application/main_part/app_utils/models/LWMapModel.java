package mapEditor.application.main_part.app_utils.models;

/**
 *
 * Created by razvanolar on 27.02.2016.
 */
public class LWMapModel {

  private String name;
  private String relativePath;
  private int rows;
  private int columns;

  public LWMapModel(String name, String relativePath, int rows, int columns) {
    this.name = name;
    this.relativePath = relativePath;
    this.rows = rows;
    this.columns = columns;
  }

  public String getName() {
    return name;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public void setColumns(int columns) {
    this.columns = columns;
  }
}

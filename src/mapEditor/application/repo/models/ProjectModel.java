package mapEditor.application.repo.models;

import mapEditor.application.repo.types.MapType;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class ProjectModel {

  private String name;
  private String homePath;
  private MapType mapType;
  private int cellSize;

  public ProjectModel() {}

  public ProjectModel(String name, String homePath, MapType mapType, int cellSize) {
    this.name = name;
    this.homePath = homePath;
    this.mapType = mapType;
    this.cellSize = cellSize;
  }

  public String getName() {
    return name;
  }

  public String getHomePath() {
    return homePath;
  }

  public MapType getMapType() {
    return mapType;
  }

  public int getCellSize() {
    return cellSize;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setHomePath(String homePath) {
    this.homePath = homePath;
  }

  public void setMapType(MapType mapType) {
    this.mapType = mapType;
  }

  public void setCellSize(int cellSize) {
    this.cellSize = cellSize;
  }
}

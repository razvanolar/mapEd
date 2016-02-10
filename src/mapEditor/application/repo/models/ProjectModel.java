package mapEditor.application.repo.models;

import mapEditor.application.repo.types.MapType;

import java.io.File;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class ProjectModel {

  private String name;
  private String homePath;
  private MapType mapType;
  private int cellSize;

  private File tileGroupsFile;
  private File tileSetsFile;
  private File tilesFile;
  private File charactersFile;
  private File mapsFile;

  public ProjectModel() {}

  public ProjectModel(String name, String homePath, MapType mapType, int cellSize) {
    this.name = name;
    this.homePath = homePath;
    this.mapType = mapType;
    this.cellSize = cellSize;

    if (!this.homePath.endsWith("\\"))
      this.homePath += "\\";
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

  public File getTileGroupsFile() {
    return tileGroupsFile;
  }

  public File getCharactersFile() {
    return charactersFile;
  }

  public File getMapsFile() {
    return mapsFile;
  }

  public File getTileSetsFile() {
    return tileSetsFile;
  }

  public File getTilesFile() {
    return tilesFile;
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

  public void setTileGroupsFile(File tileGroupsFile) {
    this.tileGroupsFile = tileGroupsFile;
  }

  public void setCharactersFile(File charactersFile) {
    this.charactersFile = charactersFile;
  }

  public void setMapsFile(File mapsFile) {
    this.mapsFile = mapsFile;
  }

  public void setTileSetsFile(File tileSetsFile) {
    this.tileSetsFile = tileSetsFile;
  }

  public void setTilesFile(File tilesFile) {
    this.tilesFile = tilesFile;
  }
}

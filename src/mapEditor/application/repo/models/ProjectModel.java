package mapEditor.application.repo.models;

import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.LWMapModel;
import mapEditor.application.main_part.app_utils.models.MapDetail;
import mapEditor.application.main_part.app_utils.models.TabKey;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.AbstractTabContainer;
import mapEditor.application.repo.HexCounter;
import mapEditor.application.repo.SystemParameters;
import mapEditor.application.repo.types.MapType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class ProjectModel {

  private String name;
  private String homePath;
  private MapType mapType;
  private int cellSize;
  private String hexValue = HexCounter.DEFAULT_VALUE;

  private File tileGroupsFile;
  private File tileSetsFile;
  private File tilesFile;
  private File charactersFile;
  private File mapsFile;

  private List<LWMapModel> lwMapModels;
  private List<MapDetail> mapDetails = new ArrayList<>();
  private Map<TabKey, List<ImageModel>> openedTileTabs = new HashMap<>();

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

  public String getHexValue() {
    return hexValue;
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

  public List<MapDetail> getMapDetails() {
    return mapDetails;
  }

  public Map<TabKey, List<ImageModel>> getOpenedTileTabs() {
    return openedTileTabs;
  }

  public List<LWMapModel> getLwMapModels() {
    return lwMapModels;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setHomePath(String homePath) {
    this.homePath = homePath;
    if (!this.homePath.endsWith("\\"))
      this.homePath += "\\";
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

  public void setHexValue(String hexValue) {
    this.hexValue = hexValue;
  }

  public void setLwMapModels(List<LWMapModel> lwMapModels) {
    this.lwMapModels = lwMapModels;
  }

  /**
   * @return the absolute path of project config file (.med file)
   */
  public String getConfigFilePath() {
    return homePath + name + SystemParameters.PROJECT_FILE_EXT;
  }

  /**
   * Calculate and return the next hex project value.
   * @return hex string
   */
  public String nextHexValue() {
    String value = HexCounter.getNextValue(hexValue);
    if (value != null)
      hexValue = value;
    return getHexValue();
  }

  public void addMapModel(MapDetail mapDetail) {
    if (mapDetail != null && !mapDetails.contains(mapDetail))
      mapDetails.add(mapDetail);
  }

  public void removeMapModel(MapDetail mapDetail) {
    if (mapDetail != null && mapDetails.contains(mapDetail))
      mapDetails.remove(mapDetail);
  }

  public void addTileForTileTabKey(TabKey key, ImageModel tile) {
    if (key != null && tile != null) {
      List<ImageModel> tiles = openedTileTabs.get(key);
      if (tiles == null) {
        tiles = new ArrayList<>();
        tiles.add(tile);
        openedTileTabs.put(key, tiles);
      } else if (!tiles.contains(tile)) {
        tiles.add(tile);
      }
    }
  }

  public void removeTileTabKey(TabKey key) {
    if (key != null && openedTileTabs.containsKey(key))
      openedTileTabs.remove(key);
  }
}

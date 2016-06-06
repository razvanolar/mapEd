package mapEditor.application.repo.models;

import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.models.LWMapModel;
import mapEditor.application.main_part.app_utils.models.MapDetail;
import mapEditor.application.main_part.app_utils.models.TabKey;
import mapEditor.application.repo.HexCounter;
import mapEditor.application.repo.SystemParameters;
import mapEditor.application.repo.types.MapType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
  private File brushesFile;
  private File objectsFile;
  private File charactersFile;
  private File mapsFile;

  private List<LWMapModel> lwMapModels;
  private List<MapDetail> mapDetails = new ArrayList<>();
  private CustomMap<TabKey, List<File>> openedTileTabs = new CustomMap<>();

  private boolean is2DVisibilitySelected;
  private boolean isGridVisibilitySelected;
  private boolean fillArea;
  private boolean showGrid = SystemParameters.DEFAULT_SHOW_GRID_VALUE;
  private boolean showProjectTree = SystemParameters.DEFAULT_SHOW_PROJECT_TREE_VALUE;

  private double projectTreeDividerPosition = .4;
  private double mapViewDividerPosition = .7;

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

  public File getBrushesFile() {
    return brushesFile;
  }

  public File getObjectsFile() {
    return objectsFile;
  }

  public List<MapDetail> getMapDetails() {
    return mapDetails;
  }

  public CustomMap<TabKey, List<File>> getOpenedTileTabs() {
    return openedTileTabs;
  }

  public List<LWMapModel> getLwMapModels() {
    return lwMapModels;
  }

  public boolean is2DVisibilitySelected() {
    return is2DVisibilitySelected;
  }

  public boolean isGridVisibilitySelected() {
    return isGridVisibilitySelected;
  }

  public boolean isFillArea() {
    return fillArea;
  }

  public boolean isShowGrid() {
    return showGrid;
  }

  public boolean isShowProjectTree() {
    return showProjectTree;
  }

  public double getProjectTreeDividerPosition() {
    return projectTreeDividerPosition;
  }

  public double getMapViewDividerPosition() {
    return mapViewDividerPosition;
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

  public void setBrushesFile(File brushesFile) {
    this.brushesFile = brushesFile;
  }

  public void setObjectsFile(File objectsFile) {
    this.objectsFile = objectsFile;
  }

  public void setHexValue(String hexValue) {
    this.hexValue = hexValue;
  }

  public void setLwMapModels(List<LWMapModel> lwMapModels) {
    this.lwMapModels = lwMapModels;
  }

  public void setIs2DVisibilitySelected(boolean is2DVisibilitySelected) {
    this.is2DVisibilitySelected = is2DVisibilitySelected;
  }

  public void setIsGridVisibilitySelected(boolean isGridVisibilitySelected) {
    this.isGridVisibilitySelected = isGridVisibilitySelected;
  }

  public void setFillArea(boolean fillArea) {
    this.fillArea = fillArea;
  }

  public void setShowGrid(boolean showGrid) {
    this.showGrid = showGrid;
  }

  public void setShowProjectTree(boolean showProjectTree) {
    this.showProjectTree = showProjectTree;
  }

  public void setProjectTreeDividerPosition(double projectTreeDividerPosition) {
    this.projectTreeDividerPosition = projectTreeDividerPosition;
  }

  public void setMapViewDividerPosition(double mapViewDividerPosition) {
    this.mapViewDividerPosition = mapViewDividerPosition;
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

  public void addTileForTileTabKey(TabKey key, File tile) {
    if (key != null && tile != null) {
      List<File> tiles = openedTileTabs.get(key);
      if (tiles == null) {
        tiles = new ArrayList<>();
        tiles.add(tile);
        openedTileTabs.put(key, tiles);
      } else if (!tiles.contains(tile)) {
        tiles.add(tile);
      }
    }
  }

  public void addTilesForTileTabKey(TabKey key, List<File> tiles) {
    if (key != null && tiles != null) {
      openedTileTabs.put(key, tiles);
    }
  }

  public void removeTileTabKey(TabKey key) {
    if (key != null && openedTileTabs.contains(key))
      openedTileTabs.remove(key);
  }
}

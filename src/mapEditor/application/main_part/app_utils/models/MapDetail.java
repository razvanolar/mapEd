package mapEditor.application.main_part.app_utils.models;

import javafx.scene.paint.Color;
import mapEditor.application.repo.types.MapType;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by razvanolar on 26.02.2016.
 */
public class MapDetail {

  private String name;
  private String absolutePath;
  private String relativePath;
  private int x;
  private int y;
  private int rows;
  private int columns;
  private int zoomStatus;
  private Color backgroundColor;
  private Color gridColor;
  private Color squareColor;
  private MapType type;
  private List<LayerModel> layers;
  private MapTilesInfo mapTilesInfo;
  private boolean showGrid;
  private boolean selected;

  private DiskIndexedTilesModel diskIndexedTilesModel;

  public MapDetail() {}

  public MapDetail(String name, String absolutePath, String relativePath, int rows, int columns,
                   Color backgroundColor, Color gridColor, Color squareColor, MapType type) {
    this.name = name;
    computeAbsolutePath(absolutePath);
    this.relativePath = relativePath;
    this.rows = rows;
    this.columns = columns;
    this.backgroundColor = backgroundColor;
    this.gridColor = gridColor;
    this.squareColor = squareColor;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getAbsolutePath() {
    return absolutePath;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  public int getZoomStatus() {
    return zoomStatus;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public Color getGridColor() {
    return gridColor;
  }

  public Color getSquareColor() {
    return squareColor;
  }

  public MapType getType() {
    return type;
  }

  public MapTilesInfo getMapTilesInfo() {
    return mapTilesInfo;
  }

  public boolean isShowGrid() {
    return showGrid;
  }

  public boolean isSelected() {
    return selected;
  }

  public List<LayerModel> getLayers() {
    return layers;
  }

  public DiskIndexedTilesModel getDiskIndexedTilesModel() {
    return diskIndexedTilesModel;
  }




  public void setName(String name) {
    this.name = name;
  }

  public void setAbsolutePath(String absolutePath) {
    computeAbsolutePath(absolutePath);
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public void setColumns(int columns) {
    this.columns = columns;
  }

  public void setZoomStatus(int zoomStatus) {
    this.zoomStatus = zoomStatus;
  }

  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public void setGridColor(Color gridColor) {
    this.gridColor = gridColor;
  }

  public void setSquareColor(Color squareColor) {
    this.squareColor = squareColor;
  }

  public void setType(MapType type) {
    this.type = type;
  }

  public void setMapTilesInfo(MapTilesInfo mapTilesInfo) {
    this.mapTilesInfo = mapTilesInfo;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public void setShowGrid(boolean showGrid) {
    this.showGrid = showGrid;
  }

  public void setDiskIndexedTilesModel(DiskIndexedTilesModel diskIndexedTilesModel) {
    this.diskIndexedTilesModel = diskIndexedTilesModel;
  }




  public void addLayer(LayerModel layer) {
    if (layer == null)
      return;
    if (layers == null)
      layers = new LinkedList<>();
    if (!layers.contains(layer))
      layers.add(layer);
  }

  public void removeLayer(LayerModel layer) {
    if (layer != null && layers != null && layers.contains(layer))
      layers.remove(layer);
  }

  public MapDetail duplicate() {
    MapDetail result = new MapDetail();
    result.name = name;
    result.absolutePath = absolutePath;
    result.relativePath = relativePath;
    result.x = x;
    result.y = y;
    result.rows = rows;
    result.columns = columns;
    result.zoomStatus = zoomStatus;
    result.backgroundColor = backgroundColor;
    result.gridColor = gridColor;
    result.squareColor = squareColor;
    result.type = type;
    result.layers = layers;
    result.mapTilesInfo = mapTilesInfo;
    result.diskIndexedTilesModel = diskIndexedTilesModel;
    return result;
  }

  @Override
  public String toString() {
    return name + " - " + absolutePath;
  }

  private void computeAbsolutePath(String path) {
    absolutePath = path;
    if (absolutePath != null && !absolutePath.endsWith("\\"))
      absolutePath += "\\";
  }
}

package mapEditor.application.main_part.app_utils.models;

import javafx.scene.paint.Color;
import mapEditor.application.repo.types.MapType;

/**
 *
 * Created by razvanolar on 26.02.2016.
 */
public class MapModel {

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
  private boolean selected;

  public MapModel() {}

  public MapModel(String name, String absolutePath, String relativePath, int rows, int columns,
                  Color backgroundColor, Color gridColor, Color squareColor, MapType type) {
    this.name = name;
    this.absolutePath = absolutePath;
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

  public boolean isSelected() {
    return selected;
  }



  public void setName(String name) {
    this.name = name;
  }

  public void setAbsolutePath(String absolutePath) {
    this.absolutePath = absolutePath;
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

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  @Override
  public String toString() {
    return name + " - " + absolutePath;
  }
}

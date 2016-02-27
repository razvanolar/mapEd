package mapEditor.application.main_part.app_utils.models;

import javafx.scene.paint.Color;
import mapEditor.application.repo.types.MapType;

/**
 *
 * Created by razvanolar on 26.02.2016.
 */
public class MapDetailsModel {

  private String name;
  private String path;
  private int rows;
  private int columns;
  private Color backgroundColor;
  private Color gridColor;
  private Color squareColor;
  private MapType type;

  public MapDetailsModel(String name, String path, int rows, int columns, Color backgroundColor, Color gridColor,
                         Color squareColor, MapType type) {
    this.name = name;
    this.path = path;
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

  public String getPath() {
    return path;
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
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

  public void setName(String name) {
    this.name = name;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public void setColumns(int columns) {
    this.columns = columns;
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
}

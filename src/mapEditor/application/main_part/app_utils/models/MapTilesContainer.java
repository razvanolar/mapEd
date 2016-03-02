package mapEditor.application.main_part.app_utils.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to store a map tiles per layers.
 * Created by razvanolar on 02.03.2016.
 */
public class MapTilesContainer {

  public class TilesMatrix {
    ImageModel[][] tilesMatrix;
    public TilesMatrix() {
      tilesMatrix = new ImageModel[rows][cols];
    }

    public ImageModel[][] getTilesMatrix() {
      return tilesMatrix;
    }
  }

  private int rows;
  private int cols;
  private Map<LayerModel, TilesMatrix> tilesMap;

  public MapTilesContainer(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
  }

  public void addTile(ImageModel tile, LayerModel layer, int row, int col) {
    if (row < 0 || col < 0 || row >= rows || col >= cols || layer == null)
      return;

    if (tilesMap == null)
      tilesMap = new HashMap<>();
    TilesMatrix matrix = tilesMap.get(layer);
    if (matrix == null) {
      matrix = new TilesMatrix();
      tilesMap.put(layer, matrix);
    }

    matrix.tilesMatrix[row][col] = tile;
  }

  public ImageModel[][] getTilesForLayer(LayerModel layer) {
    if (tilesMap == null || layer == null)
      return null;
    TilesMatrix tilesMatrix = tilesMap.get(layer);
    if (tilesMatrix == null)
      return null;
    return tilesMatrix.tilesMatrix;
  }

  public Map<LayerModel, TilesMatrix> getTilesMap() {
    return tilesMap;
  }
}

package mapEditor.application.main_part.app_utils.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to store a map tiles per layers.
 * Use it only on the UI side.
 * The tileMap does not store the layers into a particular order. So, to display the tiles per layer into the correct
 *  order, use the tile list from the MapDetails
 *
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

  public MapTilesInfo getMapInfo(List<LayerModel> layers) {
    if (tilesMap == null || layers == null || layers.isEmpty())
      return null;

    MapTilesInfo mapTilesInfo = new MapTilesInfo(layers);
    for (LayerModel layer : layers) {
      TilesMatrix tilesMatrix = tilesMap.get(layer);
      if (tilesMatrix == null)
        continue;
      ImageModel[][] matrix = tilesMatrix.getTilesMatrix();
      for (int i=0; i<rows; i++) {
        for (int j=0; j<cols; j++) {
          if (matrix[i][j] != null)
            mapTilesInfo.addTileForLayer(layer, matrix[i][j], j, i);
        }
      }
    }
    return mapTilesInfo;
  }
}
